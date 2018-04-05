(ns aoc2016.day11
  (:require [clojure.math.combinatorics :as combo]
            [clojure.set :as set]
            [clojure.string :as str]))


;;; Part 1

;; You come upon a column of four floors that have been entirely sealed off
;; from the rest of the building except for a small dedicated lobby. There are
;; some radiation warnings and a big sign which reads "Radioisotope Testing
;; Facility".

;; According to the project status board, this facility is currently being
;; used to experiment with Radioisotope Thermoelectric Generators (RTGs, or
;; simply "generators") that are designed to be paired with
;; specially-constructed microchips. Basically, an RTG is a highly radioactive
;; rock that generates electricity through heat.

;; The experimental RTGs have poor radiation containment, so they're
;; dangerously radioactive. The chips are prototypes and don't have normal
;; radiation shielding, but they do have the ability to generate an
;; electromagnetic radiation shield when powered. Unfortunately, they can only
;; be powered by their corresponding RTG. An RTG powering a microchip is still
;; dangerous to other microchips.

;; In other words, if a chip is ever left in the same area as another RTG, and
;; it's not connected to its own RTG, the chip will be fried. Therefore, it is
;; assumed that you will follow procedure and keep chips connected to their
;; corresponding RTG when they're in the same room, and away from other RTGs
;; otherwise.

;; These microchips sound very interesting and useful to your current
;; activities, and you'd like to try to retrieve them. The fourth floor of the
;; facility has an assembling machine which can make a self-contained,
;; shielded computer for you to take with you - that is, if you can bring it
;; all of the RTGs and microchips.

;; Within the radiation-shielded part of the facility (in which it's safe to
;; have these pre-assembly RTGs), there is an elevator that can move between
;; the four floors. Its capacity rating means it can carry at most yourself
;; and two RTGs or microchips in any combination. (They're rigged to some
;; heavy diagnostic equipment - the assembling machine will detach it for
;; you.) As a security measure, the elevator will only function if it contains
;; at least one RTG or microchip. The elevator always stops on each floor to
;; recharge, and this takes long enough that the items within it and the items
;; on that floor can irradiate each other. (You can prevent this if a
;; Microchip and its Generator end up on the same floor in this way, as they
;; can be connected while the elevator is recharging.)

;; You make some notes of the locations of each component of interest (your
;; puzzle input). Before you don a hazmat suit and start moving things around,
;; you'd like to have an idea of what you need to do.

;; When you enter the containment area, you and the elevator will start on the
;; first floor.

;; For example, suppose the isolated area has the following arrangement:

;; - The first floor contains a hydrogen-compatible microchip and a
;;   lithium-compatible microchip.

;; - The second floor contains a hydrogen generator.

;; - The third floor contains a lithium generator.

;; - The fourth floor contains nothing relevant.

;; As a diagram (F# for a Floor number, E for Elevator, H for Hydrogen, L for
;; Lithium, M for Microchip, and G for Generator), the initial state looks
;; like this:

;; F4 .  .  .  .  .
;; F3 .  .  .  LG .
;; F2 .  HG .  .  .
;; F1 E  .  HM .  LM

;; Then, to get everything up to the assembling machine on the fourth floor,
;; the following steps could be taken:

;; Bring the Hydrogen-compatible Microchip to the second floor, which is safe
;; because it can get power from the Hydrogen Generator:

;; F4 .  .  .  .  .
;; F3 .  .  .  LG .
;; F2 E  HG HM .  .
;; F1 .  .  .  .  LM

;; - Bring both Hydrogen-related items to the third floor, which is safe
;;   because the Hydrogen-compatible microchip is getting power from its
;;   generator:

;; F4 .  .  .  .  .
;; F3 E  HG HM LG .
;; F2 .  .  .  .  .
;; F1 .  .  .  .  LM

;; - Leave the Hydrogen Generator on floor three, but bring the
;;   Hydrogen-compatible Microchip back down with you so you can still use the
;;   elevator:

;; F4 .  .  .  .  .
;; F3 .  HG .  LG .
;; F2 E  .  HM .  .
;; F1 .  .  .  .  LM

;; - At the first floor, grab the Lithium-compatible Microchip, which is safe
;;   because Microchips don't affect each other:

;; F4 .  .  .  .  .
;; F3 .  HG .  LG .
;; F2 .  .  .  .  .
;; F1 E  .  HM .  LM

;; - Bring both Microchips up one floor, where there is nothing to fry them:

;; F4 .  .  .  .  .
;; F3 .  HG .  LG .
;; F2 E  .  HM .  LM
;; F1 .  .  .  .  .

;; - Bring both Microchips up again to floor three, where they can be
;;   temporarily connected to their corresponding generators while the
;;   elevator recharges, preventing either of them from being fried:

;; F4 .  .  .  .  .
;; F3 E  HG HM LG LM
;; F2 .  .  .  .  .
;; F1 .  .  .  .  .

;; Bring both Microchips to the fourth floor:

;; F4 E  .  HM .  LM
;; F3 .  HG .  LG .
;; F2 .  .  .  .  .
;; F1 .  .  .  .  .

;; - Leave the Lithium-compatible microchip on the fourth floor, but bring the
;;   Hydrogen-compatible one so you can still use the elevator; this is safe
;;   because although the Lithium Generator is on the destination floor, you
;;   can connect Hydrogen-compatible microchip to the Hydrogen Generator
;;   there:

;; F4 .  .  .  .  LM
;; F3 E  HG HM LG .
;; F2 .  .  .  .  .
;; F1 .  .  .  .  .

;; - Bring both Generators up to the fourth floor, which is safe because you
;;   can connect the Lithium-compatible Microchip to the Lithium Generator upon
;;   arrival:

;; F4 E  HG .  LG LM
;; F3 .  .  HM .  .
;; F2 .  .  .  .  .
;; F1 .  .  .  .  .

;; - Bring the Lithium Microchip with you to the third floor so you can use the
;;   elevator:

;; F4 .  HG .  LG .
;; F3 E  .  HM .  LM
;; F2 .  .  .  .  .
;; F1 .  .  .  .  .

;; - Bring both Microchips to the fourth floor:

;; F4 E  HG HM LG LM
;; F3 .  .  .  .  .
;; F2 .  .  .  .  .
;; F1 .  .  .  .  .

;; In this arrangement, it takes 11 steps to collect all of the objects at the
;; fourth floor for assembly. (Each elevator stop counts as one step, even if
;; nothing is added to or removed from it.)

;; In your situation, what is the minimum number of steps required to bring
;; all of the objects to the fourth floor?

(defn parse-line
  [line]
  (let [generators (->> line
                        (re-seq #"(\w+) generator")
                        (map #(vector :generator (second %))))
        microchips (->> line
                        (re-seq #"(\w+)-compatible microchip")
                        (map #(vector :microchip (second %))))]
    (into #{} (concat generators microchips))))

(defn parse-input
  [input]
  (->> input
       (str/split-lines)
       (mapv parse-line)))

(defn invalid-floor?
  [state k]
  (let [floor (nth state k)
        generators (into #{} (for [[tag name] floor
                                   :when (= :generator tag)]
                               name))
        microchips (into #{} (for [[tag name] floor
                                   :when (= :microchip tag)]
                               name))]
    (and (not-empty generators)
         (not-empty (set/difference microchips generators)))))

(defn end-state?
  "Return true if floors are in end state.

  End state is when every floor except the last (fourth) is empty."
  [state]
  (every? empty? (butlast state)))

(defn combinations
  "Return combinations of xs containing 1 or 2 items."
  [xs]
  (concat (combo/combinations (seq xs) 1)
          (combo/combinations xs 2)))

(defn remove-from-floor
  [state k vs]
  (update state k #(apply disj %1 %2) vs))

(defn add-to-floor
  [state k vs]
  (update state k #(apply conj %1 %2) vs))

(defn equivalent-representation
  "Return a representation of state that is equivalent to others modulo chip /
  generator names."
  [state]
  (->> (for [[floor items] (map-indexed #(vector %1 %2) state)
             [tag name] items]
         [tag name floor])
       (group-by second)
       vals
       (map sort)
       (map (fn [[[_ _ generator-floor]
                 [_ _ microchip-floor]]]
              [microchip-floor generator-floor]))
       frequencies))

(defn new-states
  ([state elevator k known]
   (for [items (combinations (nth state elevator))
         new-elevator (case elevator
                        3 [2]
                        0 [1]
                        [(dec elevator) (inc elevator)])
         :let [new-state (-> state
                             (remove-from-floor elevator items)
                             (add-to-floor new-elevator items))]
         :when (not (invalid-floor? new-state elevator))
         :when (not (invalid-floor? new-state new-elevator))
         :when (not (known [(equivalent-representation new-state)
                            new-elevator]))]
     [new-state new-elevator (inc k)])))

(defn num-moves
  ([states]
   (num-moves states #{} 0))
  ([states known n]
   (when (not-empty states)
     (let [[state elevator k] (first states)]
       (if (end-state? state)
         k
         (let [new-states (new-states state elevator k known)]
           (recur (into (subvec states 1) new-states)
                  (into known (for [[state elevator _] new-states]
                                [(equivalent-representation state) elevator]))
                  (inc n))))))))


;;; Part 2

;;; You step into the cleanroom separating the lobby from the isolated area
;;; and put on the hazmat suit.

;; Upon entering the isolated containment area, however, you notice some extra
;; parts on the first floor that weren't listed on the record outside:

;; - An elerium generator.

;; - An elerium-compatible microchip.

;; - A dilithium generator.

;; - A dilithium-compatible microchip.

;; These work just like the other generators and microchips. You'll have to
;; get them up to assembly as well.

;; What is the minimum number of steps required to bring all of the objects,
;; including these four new ones, to the fourth floor?

(def new-components #{[:microchip "elerium"]
                      [:generator "elerium"]
                      [:microchip "dilithium"]
                      [:generator "dilithium"]})

(defn add-to-first-floor
  [state compoments]
  (update state 0 into compoments))

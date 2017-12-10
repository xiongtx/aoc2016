(ns aoc2016.day1
  (:require [clojure.string :as str]))


;;; Part 1

;; You're airdropped near Easter Bunny Headquarters in a city
;; somewhere. "Near", unfortunately, is as close as you can get - the
;; instructions on the Easter Bunny Recruiting Document the Elves intercepted
;; start here, and nobody had time to work them out further.

;; The Document indicates that you should start at the given
;; coordinates (where you just landed) and face North. Then, follow the
;; provided sequence: either turn left (L) or right (R) 90 degrees, then walk
;; forward the given number of blocks, ending at a new intersection.

;; There's no time to follow such ridiculous instructions on foot, though, so
;; you take a moment and work out the destination. Given that you can only
;; walk on the street grid of the city, how far is the shortest path to the
;; destination?

;; For example:

;; - Following R2, L3 leaves you 2 blocks East and 3 blocks North, or 5 blocks
;;   away.

;; - R2, R2, R2 leaves you 2 blocks due South of your starting position, which
;;   is 2 blocks away.

;; - R5, L5, R5, R3 leaves you 12 blocks away.

;; How many blocks away is Easter Bunny HQ?

(def directions [:N :E :S :W])

(defn parse-instruction
  "Parse a single instruction.

  E.g. \"R2\" => [:R 2]"
  [instruction]
  (let [[_ turn steps] (re-find #"([RL])([0-9].*)" instruction)]
    [(keyword turn) (Long/parseLong steps)]))

(defn parse-input
  [input]
  (->> (str/split input #", ")
       (map parse-instruction)))

(defn change-direction
  "Return new direction given direction and turn (L/R)."
  [direction turn]
  (let [index (.indexOf directions direction)]
    (nth directions (if (= :R turn)
                      (mod (inc index) 4)
                      (mod (dec index) 4)))))

(defn coordinate-change
  "Return [dx dy] given current direction, turn, and number of steps.

  E.g.

  - :N, :R, 12 => [12 0]
  - :W, :L,  3 => [0 -3]"
  [direction turn steps]
  (case (change-direction direction turn)
    :N [0 steps]
    :E [steps 0]
    :S [0 (- steps)]
    :W [(- steps) 0]))

(defn distance
  [instructions]
  (->> instructions
       (reduce (fn [[dir coords] [turn steps]]
                 (let [delta (coordinate-change dir turn steps)]
                   [(change-direction dir turn) (map + coords delta)]))
               [:N [0 0]])
       second
       (map #(Math/abs %))
       (apply +)))


;;; Part 2

;; Then, you notice the instructions continue on the back of the Recruiting
;; Document. Easter Bunny HQ is actually at the first location you visit
;; twice.

;; For example, if your instructions are R8, R4, R4, R8, the first location
;; you visit twice is 4 blocks away, due East.

;; How many blocks away is the first location you visit twice?

(defn new-coordinates
  [[x y] [dx dy]]
  (if (zero? dx)
    (let [d (* (Long/signum dy) 1)]
      (for [j (range (+ y d) (+ y d dy) d)]
        [x j]))
    (let [d (* (Long/signum dx) 1)]
      (for [i (range (+ x d) (+ x d dx) d)]
        [i y]))))

(defn visiting-again
  [visited coordinates]
  (reduce (fn [[v _] c]
            (if (contains? v c)
              (reduced [v c])
              [(conj v c) nil]))
          [visited nil]
          coordinates))

(defn revisited
  [instructions]
  (->> instructions
       (reduce (fn [[dir coords visited] [turn steps]]
                 (let [delta (coordinate-change dir turn steps)
                       cs (new-coordinates coords delta)
                       [visited c?] (visiting-again visited cs)
                       next [(change-direction dir turn)
                             (map + coords delta)
                             visited
                             c?]]
                   (if c? (reduced next) next)))
               [:N [0 0] #{[0 0]} nil])
       last))

(defn revisited-distance
  [instructions]
  (some->> (revisited instructions)
           (map #(Math/abs %))
           (apply +)))

(ns aoc2016.day17
  (:require [clojure.string :as str])
  (:import [java.security MessageDigest]
           [java.util PriorityQueue]
           [javax.xml.bind DatatypeConverter]))


;;; Part 1

;; You're trying to access a secure vault protected by a 4x4 grid of small
;; rooms connected by doors. You start in the top-left room (marked S), and
;; you can access the vault (marked V) once you reach the bottom-right room:

;; #########
;; #S| | | #
;; #-#-#-#-#
;; # | | | #
;; #-#-#-#-#
;; # | | | #
;; #-#-#-#-#
;; # | | |
;; ####### V

;; Fixed walls are marked with #, and doors are marked with - or |.

;; The doors in your current room are either open or closed (and locked) based
;; on the hexadecimal MD5 hash of a passcode (your puzzle input) followed by a
;; sequence of uppercase characters representing the path you have taken so
;; far (U for up, D for down, L for left, and R for right).

;; Only the first four characters of the hash are used; they represent,
;; respectively, the doors up, down, left, and right from your current
;; position. Any b, c, d, e, or f means that the corresponding door is open;
;; any other character (any number or a) means that the corresponding door is
;; closed and locked.

;; To access the vault, all you need to do is reach the bottom-right room;
;; reaching this room opens the vault and all doors in the maze.

;; For example, suppose the passcode is hijkl. Initially, you have taken no
;; steps, and so your path is empty: you simply find the MD5 hash of hijkl
;; alone. The first four characters of this hash are ced9, which indicate that
;; up is open (c), down is open (e), left is open (d), and right is closed and
;; locked (9). Because you start in the top-left corner, there are no "up"
;; or "left" doors to be open, so your only choice is down.

;; Next, having gone only one step (down, or D), you find the hash of
;; hijklD. This produces f2bc, which indicates that you can go back up,
;; left (but that's a wall), or right. Going right means hashing hijklDR to
;; get 5745 - all doors closed and locked. However, going up instead is
;; worthwhile: even though it returns you to the room you started in, your
;; path would then be DU, opening a different set of doors.

;; After going DU (and then hashing hijklDU to get 528e), only the right door
;; is open; after going DUR, all doors lock. (Fortunately, your actual
;; passcode is not hijkl).

;; Passcodes actually used by Easter Bunny Vault Security do allow access to
;; the vault if you know the right path. For example:

;; - If your passcode were ihgpwlah, the shortest path would be DDRRRD.

;; - With kglvqrro, the shortest path would be DDUDRLRRUDRD.

;; - With ulqzkmiv, the shortest would be DRURDRUDDLLDLUURRDULRLDUUDDDRR.

;; Given your vault's passcode, what is the shortest path (the actual path,
;; not just the length) to reach the vault?

(defn parse-input
  [input]
  (str/trim input))

(def md (MessageDigest/getInstance "md5"))

(defn md5
  "Return MD5 hash of string s in lower case."
  [s]
  (->> (.getBytes s)
       (.digest md)
       DatatypeConverter/printHexBinary
       str/lower-case))

(defn manhattan
  [[r1 c1] [r2 c2]]
  (+ (Math/abs (- r2 r1))
     (Math/abs (- c2 c1))))

(defrecord Position [path pos passcode])

(defn possible-moves
  [{:keys [path passcode]
    [r c] :pos}]
  (let [[up down left right] (md5 (str passcode path))]
    (for [[i j ch ch2] [[(inc r) c down \D]
                        [(dec r) c up \U]
                        [r (dec c) left \L]
                        [r (inc c) right \R]]
          :when (<= 0 i 3)
          :when (<= 0 j 3)
          :when (#{\b \c \d \e \f} ch)]
      (->Position (str path ch2) [i j] passcode))))

(defn priority
  [{:keys [path pos]}]
  (+ (count path) (manhattan pos [3 3])))

(defn pq-compare
  [position-1 position-2]
  (< (priority position-1) (priority position-2)))

(defn shortest-path
  ([passcode]
   (shortest-path passcode [0 0] [3 3]))
  ([passcode start goal]
   (loop [q (doto (PriorityQueue. 20 pq-compare)
              (.add (->Position "" start passcode)))]
     (when-not (.isEmpty q)
       (let [{:keys [path pos passcode] :as position} (.poll q)]
         (if (= goal pos)
           path
           (recur (doto q (.addAll (possible-moves position))))))))))


;;; Part 2

;; You're curious how robust this security solution really is, and so you
;; decide to find longer and longer paths which still provide access to the
;; vault. You remember that paths always end the first time they reach the
;; bottom-right room (that is, they can never pass through it, only end in
;; it).

;; For example:

;; - If your passcode were ihgpwlah, the longest path would take 370 steps.

;; - With kglvqrro, the longest path would be 492 steps long.

;; - With ulqzkmiv, the longest path would be 830 steps long.

;; What is the length of the longest path that reaches the vault?

(defn goal?
  [pos]
  (= [3 3] pos))

(defn pq-longest
  [{:keys [pos-1] :as position-1}
   {:keys [pos-2] :as position-2}]
  (cond
    (and (goal? pos-1) (not (goal? pos-2))) 1
    (and (not (goal? pos-1)) (goal? pos-2)) -1
    :else (< (priority position-1) (priority position-2))))

(defn longest-path-length
  ([passcode]
   (longest-path-length passcode [0 0] [3 3]))
  ([passcode start goal]
   (loop [q (doto (PriorityQueue. 20 pq-longest)
              (.add (->Position "" start passcode)))
          max-goal-count 0]
     (if (.isEmpty q)
       max-goal-count
       (let [{:keys [path pos passcode] :as position} (.poll q)]
         (recur (if (goal? pos)
                  q
                  (doto q (.addAll (possible-moves position))))
                (if (and (goal? pos) (> (count path) max-goal-count))
                  (count path)
                  max-goal-count)))))))

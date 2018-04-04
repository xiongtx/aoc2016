(ns aoc2016.day13
  (:require [clojure.string :as str]))


;;; Part 1

;; You arrive at the first floor of this new building to discover a much less
;; welcoming environment than the shiny atrium of the last one. Instead, you
;; are in a maze of twisty little cubicles, all alike.

;; Every location in this area is addressed by a pair of non-negative
;; integers (x,y). Each such coordinate is either a wall or an open space. You
;; can't move diagonally. The cube maze starts at 0,0 and seems to extend
;; infinitely toward positive x and y; negative values are invalid, as they
;; represent a location outside the building. You are in a small waiting area
;; at 1,1.

;; While it seems chaotic, a nearby morale-boosting poster explains, the
;; layout is actually quite logical. You can determine whether a given x,y
;; coordinate will be a wall or an open space using a simple system:

;; - Find x*x + 3*x + 2*x*y + y + y*y.

;; - Add the office designer's favorite number (your puzzle input).

;; - Find the binary representation of that sum; count the number of bits that
;;   are 1.

;;   - If the number of bits that are 1 is even, it's an open space.

;;   - If the number of bits that are 1 is odd, it's a wall.

;; For example, if the office designer's favorite number were 10, drawing
;; walls as # and open spaces as ., the corner of the building containing 0,0
;; would look like this:

;;   0123456789
;; 0 .#.####.##
;; 1 ..#..#...#
;; 2 #....##...
;; 3 ###.#.###.
;; 4 .##..#..#.
;; 5 ..##....#.
;; 6 #...##.###

;; Now, suppose you wanted to reach 7,4. The shortest route you could take is
;; marked as O:

;;   0123456789
;; 0 .#.####.##
;; 1 .O#..#...#
;; 2 #OOO.##...
;; 3 ###O#.###.
;; 4 .##OO#OO#.
;; 5 ..##OOO.#.
;; 6 #...##.###

;; Thus, reaching 7,4 would take a minimum of 11 steps (starting from your
;; current location, 1,1).

;; What is the fewest number of steps required for you to reach 31,39?

(defn parse-input
  [input]
  (Long/parseLong (str/trim input)))

(defn space?
  [x y fav]
  (->> (+ (* x x) (* 3 x) (* 2 x y) y (* y y))
      (+ fav)
      Long/toBinaryString
      (filter #(= \1 %))
      count
      even?))

(defn possible-moves
  [x y fav]
  (for [[a b] [[(inc x) y] [(dec x) y]
               [x (inc y)] [x (dec y)]]
        :when (not-any? neg? [a b])
        :when (space? a b fav)]
    [a b]))

(defn num-moves
  ([goal fav]
   (num-moves goal fav #{[1 1]} [[1 1 0]]))
  ([goal fav known positions]
   (when-not (empty? positions)
     (let [[x y k] (first positions)]
       (if (= goal [x y])
         k
         (let [next-positions (->> (possible-moves x y fav)
                                   (filter #(not (contains? known %))))]
           (recur goal fav (into known next-positions)
                  (into (subvec positions 1)
                        (map #(conj % (inc k)) next-positions)))))))))


;;; Part 2

;; How many locations (distinct x,y coordinates, including your starting
;; location) can you reach in at most 50 steps?

(defn num-within-steps
  ([n fav]
   (num-within-steps n fav #{[1 1]}))
  ([n fav known]
   (num-within-steps n fav known [[1 1 0]]))
  ([n fav known positions]
   (num-within-steps n fav known positions 0))
  ([n fav known positions m]
   (let [[x y k] (first positions)]
     (if (or (nil? k) (> k n))
       m
       (let [next-positions (->> (possible-moves x y fav)
                                 (filter #(not (contains? known %))))]
         (recur n fav (into known next-positions)
                (into (subvec positions 1)
                      (map #(conj % (inc k))) next-positions)
                (inc m)))))))

(ns aoc2016.day13-test
  (:require [aoc2016.day13 :as sut]
            [clojure.java.io :as io]
            [clojure.test :refer :all]))

(def input
  "Puzzle input from Advent of Code, Day 13.

  https://adventofcode.com/2016/day/13/input"
  (slurp (io/resource "day13-input.txt")))

(deftest num-moves-test
  (is (= 11 (sut/num-moves [7 4] 10))))

(deftest num-moves-test
  (is (= 86 (sut/num-moves [31 39] (sut/parse-input input)))))

(deftest num-within-steps-test
  (are [e v] (= e (sut/num-within-steps v 10))
    1 0
    3 1
    5 2
    6 3
    9 4
    11 5))

(deftest part-2-test
  (is (= 127 (sut/num-within-steps 50 (sut/parse-input input)))))

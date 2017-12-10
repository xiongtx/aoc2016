(ns aoc2016.day1-test
  (:require [aoc2016.day1 :as sut]
            [clojure.java.io :as io]
            [clojure.test :refer :all]))

(def input
  "Puzzle input from Advent of Code, Day 1.

  https://adventofcode.com/2016/day/1/input"
  (slurp (io/resource "day1-input.txt")))

(deftest distance-test
  (are [e s] (= e (sut/distance (sut/parse-input s)))
    5 "R2, L3"
    2 "R2, R2, R2"
    12 "R5, L5, R5, R3"))

(deftest part-1-test
  (is (= 298 (sut/distance (sut/parse-input input)))))

(deftest revisited-distance-test
  (are [e s] (= e (sut/revisited-distance (sut/parse-input s)))
    4 "R8, R4, R4, R8"
    4 "L8, L4, L4, L8"))

(deftest part-2-test
  (is (= 158 (sut/revisited-distance (sut/parse-input input)))))

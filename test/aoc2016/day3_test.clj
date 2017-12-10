(ns aoc2016.day3-test
  (:require [aoc2016.day3 :as sut]
            [clojure.java.io :as io]
            [clojure.test :refer :all]))

(def input
  "Puzzle input from Advent of Code, Day 3.

  https://adventofcode.com/2016/day/3/input"
  (slurp (io/resource "day3-input.txt")))

(deftest part-1-test
  (is (= 993 (sut/num-valid (sut/parse-input input)))))

(deftest part-2-test
  (is (= 1849 (sut/num-valid (sut/parse-columns input)))))

(ns aoc2016.day2-test
  (:require [aoc2016.day2 :as sut]
            [clojure.java.io :as io]
            [clojure.test :refer :all]))

(def example
  "Example input from Advent of Code, Day 2."
  (slurp (io/resource "day2-example.txt")))

(def input
  "Puzzle input from Advent of Code, Day 2.

  https://adventofcode.com/2016/day/2/input"
  (slurp (io/resource "day2-input.txt")))

(deftest code-test
  (is (= "1985" (sut/code (sut/parse-input example)))))

(deftest part-1-test
  (is (= "56855" (sut/code (sut/parse-input input)))))

(deftest code-diamond-test
  (is (= "5DB3" (sut/code-diamond (sut/parse-input example)))))

(deftest part-2-test
  (is (= "B3C27" (sut/code-diamond (sut/parse-input input)))))

(ns aoc2016.day11-test
  (:require [aoc2016.day11 :as sut]
            [clojure.java.io :as io]
            [clojure.test :refer :all]))

(def input
  "Puzzle input from Advent of Code, Day 11.

  https://adventofcode.com/2016/day/11/input"
  (slurp (io/resource "day11-input.txt")))

(def example
  (slurp (io/resource "day11-example.txt")))

(deftest num-moves-test
  (is (= 11 (sut/num-moves [[(sut/parse-input example) 0 0]] #{} 0))))

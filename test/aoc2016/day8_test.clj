(ns aoc2016.day8-test
  (:require [aoc2016.day8 :as sut]
            [clojure.java.io :as io]
            [clojure.test :refer :all]))

(def input
  "Puzzle input from Advent of Code, Day 8.

  https://adventofcode.com/2016/day/8/input"
  (slurp (io/resource "day8-input.txt")))

(deftest part-1-test
  (is (= 115 (->> input
                  sut/parse-input
                  (sut/apply-instructions sut/screen)
                  sut/num-lit))))

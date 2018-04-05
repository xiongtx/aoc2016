(ns aoc2016.day15-test
  (:require [aoc2016.day15 :as sut]
            [clojure.java.io :as io]
            [clojure.test :refer :all]))

(def input
  "Puzzle input from Advent of Code, Day 15.

  https://adventofcode.com/2016/day/15/input"
  (slurp (io/resource "day15-input.txt")))

(def example
  (slurp (io/resource "day15-example.txt")))

(deftest min-wait-test
  (is (= 5 (sut/min-wait (sut/parse-input example)))))

(deftest part-1-test
  (is (= 122318 (sut/min-wait (sut/parse-input input)))))

(deftest part-2-test
  (is (= 3208583 (-> input
                     sut/parse-input
                     sut/add-new-disk
                     sut/min-wait))))

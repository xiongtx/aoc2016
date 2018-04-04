(ns aoc2016.day12-test
  (:require [aoc2016.day12 :as sut]
            [clojure.java.io :as io]
            [clojure.test :refer :all]))

(def input
  "Puzzle input from Advent of Code, Day 12.

  https://adventofcode.com/2016/day/12/input"
  (slurp (io/resource "day12-input.txt")))

(def example
  (slurp (io/resource "day12-example.txt")))

(deftest a-value-test
  (is (= 42 (sut/a-value sut/init-state (sut/parse-input example)))))

(deftest part-1-test
  (is (= 318083 (sut/a-value sut/init-state (sut/parse-input input)))))

(deftest part-2-test
  (is (= 9227737 (sut/a-value sut/init-state-2 (sut/parse-input input)))))

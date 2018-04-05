(ns aoc2016.day14-test
  (:require [aoc2016.day14 :as sut]
            [clojure.java.io :as io]
            [clojure.test :refer :all]))

(def input
  "Puzzle input from Advent of Code, Day 14.

  https://adventofcode.com/2016/day/14/input"
  (slurp (io/resource "day14-input.txt")))

;; ~12 s
(deftest index-64-key-test
  (is (= 22728 (sut/index-64-key "abc"))))

;; Too slow! ~24 s
#_(deftest part-1-test
    (is (= 35186 (sut/index-64-key (sut/parse-input input)))))

;; Too slow! ~3.6 min
#_(deftest index-64-stretched-key-test
    (is (= 22551 (sut/index-64-stretched-key "abc"))))

;; Too slow! ~4 min
#_(deftest part-2-test
    (is (= 22429 (sut/index-64-stretched-key (sut/parse-input input)))))

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

;; ~36 ms
(deftest num-moves-test
  (is (= 11 (sut/num-moves [[(sut/parse-input example) 0 0]]))))

;; ~3.5s
(deftest part-1-test
  (is (= 33 (sut/num-moves [[(sut/parse-input input) 0 0]]))))

(def init-state-2
  (sut/add-to-first-floor (sut/parse-input input) sut/new-components))

;; Too slow! ~26.5s
#_(deftest part-2-test
    (is (= 57 (sut/num-moves [[init-state-2 0 0]]))))

(ns aoc2016.day6-test
  (:require [aoc2016.day6 :as sut]
            [clojure.java.io :as io]
            [clojure.test :refer :all]))

(def input
  "Puzzle input from Advent of Code, Day 6.

  https://adventofcode.com/2016/day/6/input"
  (slurp (io/resource "day6-input.txt")))

(def example
  (slurp (io/resource "day6-example.txt")))

(deftest message-test
  (is (= "easter" (sut/message (sut/parse-input example)))))

(deftest part-1-test
  (is (= "tkspfjcc" (sut/message (sut/parse-input input)))))

(deftest message-2-test
  (is (= "advent" (sut/message-2 (sut/parse-input example)))))

(deftest part-2-test
  (is (= "xrlmbypn" (sut/message-2 (sut/parse-input input)))))

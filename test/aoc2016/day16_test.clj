(ns aoc2016.day16-test
  (:require [aoc2016.day16 :as sut]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.test :refer :all]))

(def input
  "Puzzle input from Advent of Code, Day 16.

  https://adventofcode.com/2016/day/16/input"
  (slurp (io/resource "day16-input.txt")))

(deftest dragon-curve-test
  (are [e v] (= e (str/join (sut/dragon-curve v)))
    "100" "1"
    "001" "0"
    "11111000000" "11111"
    "1111000010100101011110000" "111100001010"))

(deftest part-1-test
  (is (= "10100011010101011"
         (sut/generate-and-checksum (sut/parse-input input) 272))))

;; Too slow! ~41 s
#_(deftest part-2-test
    (is (= (sut/generate-and-checksum (sut/parse-input input) 35651584))))

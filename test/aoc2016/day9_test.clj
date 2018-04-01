(ns aoc2016.day9-test
  (:require [aoc2016.day9 :as sut]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.test :refer :all]))

(def input
  "Puzzle input from Advent of Code, Day 9.

  https://adventofcode.com/2016/day/9/input"
  (str/trim (slurp (io/resource "day9-input.txt"))))

(deftest decompressed-length-test
  (are [e v] (= e (sut/decompressed-length v))
    6 "ADVENT"
    7 "A(1x5)BC"
    9 "(3x3)XYZ"
    11 "A(2x2)BCD(2x2)EFG"
    6 "(6x1)(1x3)A"
    18 "X(8x2)(3x3)ABCY"))

(deftest part-1-test
  (is (= 98136 (sut/decompressed-length input))))

(deftest decompressed-length-2-test
  (are [e v] (= e (sut/decompressed-length v))
    9 "(3x3)XYZ"
    20 "X(8x2)(3x3)ABCY"
    241920 "(27x12)(20x12)(13x14)(7x10)(1x12)A"
    445 "(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN"))

(deftest part-2-test
  (is (= 10964557606 (sut/decompressed-length-2 input))))

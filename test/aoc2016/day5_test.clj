(ns aoc2016.day5-test
  (:require [aoc2016.day5 :as sut]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.test :refer :all]))

(def input
  "Puzzle input from Advent of Code, Day 5.

  https://adventofcode.com/2016/day/5/input"
  (str/trim (slurp (io/resource "day5-input.txt"))))

(deftest md5-test
  (is (str/starts-with? (sut/md5 "abc3231929") "000001"))
  (is (str/starts-with? (sut/md5 "abc5278568") "00000f")))

;; Too slow!

#_(deftest password-test
    (is (= "18f47a30" (sut/password "abc"))))

#_(deftest part-1-test
    (is (= "801b56a7" (sut/password input))))

#_(deftest password-2-test
    (is (= "05ace8e3" (sut/password-2 "abc"))))

#_(deftest part-2-test
    (is (= "424a0197" (sut/password-2 input))))

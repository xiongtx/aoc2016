(ns aoc2016.day17-test
  (:require [aoc2016.day17 :as sut]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.test :refer :all]))

(def input
  "Puzzle input from Advent of Code, Day 17.

  https://adventofcode.com/2016/day/17/input"
  (slurp (io/resource "day17-input.txt")))

(deftest shortest-path-test
  (are [e v] (= e (sut/shortest-path v))
    "DDRRRD" "ihgpwlah"
    "DDUDRLRRUDRD" "kglvqrro"
    "DRURDRUDDLLDLUURRDULRLDUUDDDRR" "ulqzkmiv"))

(deftest part-1-test
  (is (= "RLRDRDUDDR" (sut/shortest-path (sut/parse-input input)))))

;; Too slow!
#_(deftest longest-path-test
    (are [e v] (= e (sut/longest-path-length v))
      370 "ihgpwlah"
      492 "kglvqrro"
      830 "ulqzkmiv"))

;; Too slow! ~31 s
#_(deftest part-2-test
    (is (= 420 (sut/longest-path-length (sut/parse-input input)))))

(ns aoc2016.day10-test
  (:require [aoc2016.day10 :as sut]
            [clojure.java.io :as io]
            [clojure.test :refer :all]))

(def input
  "Puzzle input from Advent of Code, Day 10.

  https://adventofcode.com/2016/day/10/input"
  (slurp (io/resource "day10-input.txt")))

(def example
  (slurp (io/resource "day10-example.txt")))

(def input-init (-> input
                    sut/parse-input
                    sut/init-bots))

(def example-init (-> example
                      sut/parse-input
                      sut/init-bots))

(deftest find-bot-test
  (is (= "bot 2" (sut/find-bot example-init 5 2))))

(deftest part-1-test
  (is (= "bot 141" (sut/find-bot input-init 61 17))))

(deftest part-2-test
  (is (= 1209 (sut/output-chips-products input-init))))

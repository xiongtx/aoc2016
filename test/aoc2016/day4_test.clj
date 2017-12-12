(ns aoc2016.day4-test
  (:require [aoc2016.day4 :as sut]
            [clojure.java.io :as io]
            [clojure.test :refer :all]))

(def input
  "Puzzle input from Advent of Code, Day 4.

  https://adventofcode.com/2016/day/4/input"
  (slurp (io/resource "day4-input.txt")))


(deftest real-room?-test
  (are [e s] (= e (sut/real-room? (sut/parse-line s)))
    true "aaaaa-bbbb-z-y-x-123[abxyz]"
    true "a-b-c-d-e-f-g-h-987[abcde]"
    true "not-a-real-room-404[oarel]"
    false "totally-real-room-200[decoy]"))

(deftest part-1-test
  (is (= 361724 (sut/sector-id-sum (sut/parse-input input)))))

(deftest decrypt-name-test
  (is (= "very encrypted name"
         (sut/decrypt-name "qzmt-zixmtkozy-ivhz" 343))))

(deftest part-2-test
  (is (= 482 (sut/northpole-object-storage-id (sut/parse-input input)))))

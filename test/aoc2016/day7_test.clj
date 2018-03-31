(ns aoc2016.day7-test
  (:require [aoc2016.day7 :as sut]
            [clojure.java.io :as io]
            [clojure.test :refer :all]))

(def input
  "Puzzle input from Advent of Code, Day 7.

  https://adventofcode.com/2016/day/7/input"
  (slurp (io/resource "day7-input.txt")))

(deftest supports-tls?-test
  (is (sut/supports-tls? (sut/parse-ip "abba[mnop]qrst")))
  (is (not (sut/supports-tls? (sut/parse-ip "abcd[bddb]xyyx"))))
  (is (not (sut/supports-tls? (sut/parse-ip "aaaa[qwer]tyui"))))
  (is (sut/supports-tls? (sut/parse-ip "ioxxoj[asdfgh]zxcvbn"))))

(deftest part-1-test
  (is (= 118 (sut/num-supports-tls (sut/parse-input input)))))

(deftest supports-ssl?-test
  (is (sut/supports-ssl? (sut/parse-ip "aba[bab]xyz")))
  (is (not (sut/supports-ssl? (sut/parse-ip "xyx[xyx]xyx"))))
  (is (sut/supports-ssl? (sut/parse-ip "aaa[kek]eke")))
  (is (sut/supports-ssl? (sut/parse-ip "zazbz[bzb]cdb"))))

(deftest part-2-test
  (is (= 260 (sut/num-supports-ssl (sut/parse-input input)))))

(ns aoc2016.day8
  (:require [clojure.string :as str]))


;;; Part 1

;; You come across a door implementing what you can only assume is an
;; implementation of two-factor authentication after a long game of
;; requirements telephone.

;; To get past the door, you first swipe a keycard (no problem; there was one
;; on a nearby desk). Then, it displays a code on a little screen, and you
;; type that code on a keypad. Then, presumably, the door unlocks.

;; Unfortunately, the screen has been smashed. After a few minutes, you've
;; taken everything apart and figured out how it works. Now you just have to
;; work out what the screen would have displayed.

;; The magnetic strip on the card you swiped encodes a series of instructions
;; for the screen; these instructions are your puzzle input. The screen is 50
;; pixels wide and 6 pixels tall, all of which start off, and is capable of
;; three somewhat peculiar operations:

;; - rect AxB turns on all of the pixels in a rectangle at the top-left of the
;;   screen which is A wide and B tall.

;; - rotate row y=A by B shifts all of the pixels in row A (0 is the top row)
;;   right by B pixels. Pixels that would fall off the right end appear at the
;;   left end of the row.

;; - rotate column x=A by B shifts all of the pixels in column A (0 is the
;;   left column) down by B pixels. Pixels that would fall off the bottom
;;   appear at the top of the column.

;; For example, here is a simple sequence on a smaller screen:

;; rect 3x2 creates a small rectangle in the top-left corner:

;; ###....
;; ###....
;; .......

;; rotate column x=1 by 1 rotates the second column down by one pixel:

;; #.#....
;; ###....
;; .#.....

;; rotate row y=0 by 4 rotates the top row right by four pixels:

;; ....#.#
;; ###....
;; .#.....

;; rotate column x=1 by 1 again rotates the second column down by one pixel,
;; causing the bottom pixel to wrap back to the top:

;; .#..#.#
;; #.#....
;; .#.....

;; As you can see, this display technology is extremely powerful, and will
;; soon dominate the tiny-code-displaying-screen market. That's what the
;; advertisement on the back of the display tries to convince you, anyway.

;; There seems to be an intermediate check of the voltage used by the display:
;; after you swipe your card, if the screen did work, how many pixels should
;; be lit?

(defn parse-re
  [tag re s]
  (into [tag] (->> s
                   (re-find re)
                   rest
                   (map #(Long/parseLong %)))))

(def parse-rect
  (partial parse-re :rect #"rect ([0-9]+)x([0-9]+)"))

(def parse-rotate-row (partial parse-re :rotate-row
                               #"rotate row y=([0-9]+) by ([0-9]+)"))

(def parse-rotate-col (partial parse-re :rotate-col
                               #"rotate column x=([0-9]+) by ([0-9]+)"))

(defn parse-instruction
  [s]
  (cond
    (str/starts-with? s "rect") (parse-rect s)
    (str/starts-with? s "rotate row") (parse-rotate-row s)
    (str/starts-with? s "rotate column") (parse-rotate-col s)))

(defn parse-input
  [input]
  (->> input
       str/split-lines
       (map parse-instruction)))

(def screen (vec (for [_ (range 6)]
                   (vec (take 50 (repeat nil))))))

(defn rect
  [screen [_ cols rows]]
  (reduce (fn [scr [col row]]
            (assoc-in scr [row col] :on))
          screen
          (for [c (range cols)
                r (range rows)]
            [c r])))

(defn rotate
  [k coll]
  (let [n (count coll)
        k (- n (mod k n))]
    (->> coll
         cycle
         (drop k)
         (take n ))))

(defn rotate-row
  [screen [_ num-row pixels]]
  (let [row (->> num-row
                 (nth screen)
                 (rotate pixels)
                 vec)]
    (assoc screen num-row row)))

(defn rotate-col
  [screen [_ num-col pixels]]
  (let [col (->> screen
                 (map #(nth % num-col))
                 (rotate pixels)
                 vec)]
    (reduce (fn [scr row]
              (assoc-in scr [row num-col] (nth col row)))
            screen
            (range (count screen)))))

(defn execute-instruction
  [screen [tag & _ :as instr]]
  (case tag
    :rect (rect screen instr)
    :rotate-row (rotate-row screen instr)
    :rotate-col (rotate-col screen instr)))

(defn apply-instructions
  [screen instrs]
  (reduce execute-instruction screen instrs))

(defn num-lit
  [screen]
  (->> (flatten screen)
       (filter some?)
       count))


;;; Part 2

;; You notice that the screen is only capable of displaying capital letters;
;; in the font it uses, each letter is 5 pixels wide and 6 tall.

;; After you swipe your card, what code is the screen trying to display?

(defn print-letters
  [screen]
  (doseq [row screen]
    (println (str/join (map #(if (nil? %) " " "*") row)))))
;; EFEYKFRFIJ

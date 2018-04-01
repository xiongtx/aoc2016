(ns aoc2016.day10
  (:require [clojure.string :as str]))


;;; Part 1

;; You come upon a factory in which many robots are zooming around handing
;; small microchips to each other.

;; Upon closer examination, you notice that each bot only proceeds when it has
;; two microchips, and once it does, it gives each one to a different bot or
;; puts it in a marked "output" bin. Sometimes, bots take microchips
;; from "input" bins, too.

;; Inspecting one of the microchips, it seems like they each contain a single
;; number; the bots must use some logic to decide what to do with each
;; chip. You access the local control computer and download the bots'
;; instructions (your puzzle input).

;; Some of the instructions specify that a specific-valued microchip should be
;; given to a specific bot; the rest of the instructions indicate what a given
;; bot should do with its lower-value or higher-value chip.

;; For example, consider the following instructions:

;; - value 5 goes to bot 2
;; - bot 2 gives low to bot 1 and high to bot 0
;; - value 3 goes to bot 1
;; - bot 1 gives low to output 1 and high to bot 0
;; - bot 0 gives low to output 2 and high to output 0
;; - value 2 goes to bot 2

;; - Initially, bot 1 starts with a value-3 chip, and bot 2 starts with a
;;   value-2 chip and a value-5 chip.

;; - Because bot 2 has two microchips, it gives its lower one (2) to bot 1 and
;;   its higher one (5) to bot 0.

;; - Then, bot 1 has two microchips; it puts the value-2 chip in output 1 and
;;   gives the value-3 chip to bot 0.

;; - Finally, bot 0 has two microchips; it puts the 3 in output 2 and the 5 in
;;   output 0.

;; In the end, output bin 0 contains a value-5 microchip, output bin 1
;; contains a value-2 microchip, and output bin 2 contains a value-3
;; microchip. In this configuration, bot number 2 is responsible for comparing
;; value-5 microchips with value-2 microchips.

;; Based on your instructions, what is the number of the bot that is
;; responsible for comparing value-61 microchips with value-17 microchips?

(defn parse-value-instruction
  [s]
  (into [:value]
        (let [strs (->> s
                        (re-find #"value ([0-9]+) goes to (bot [0-9]+)")
                        rest)]
          (cons (Long/parseLong (first strs)) (rest strs)))))

(defn parse-bot-instruction
  [s]
  (into [:bot]
        (->> s
             (re-find #"(bot [0-9]+) gives low to ([a-z]+ [0-9]+) and high to ([a-z]+ [0-9]+)")
             rest)))

(defn parse-instruction
  [s]
  (cond
    (str/starts-with? s "value") (parse-value-instruction s)
    (str/starts-with? s "bot") (parse-bot-instruction s)))

(defn parse-input
  [input]
  (->> input
       str/split-lines
       (map parse-instruction)))

(defn add-val
  [state place val]
  (update state place update :vals (fnil conj #{}) val))

(defn init-bots
  "Initialize state of bots."
  [instrs]
  (reduce (fn [state instr]
            (if (= :value (first instr))
              (let [[_ val to] instr]
                (add-val state to val))
              (let [[_ bot low-to high-to] instr]
                (-> state
                    (update bot assoc :low-to low-to)
                    (update bot assoc :high-to high-to)))))
          {}
          instrs))

(defn bot?
  [place]
  (str/starts-with? place "bot"))

(defn execute-step
  [state]
  (if-let [[place {:keys [vals low-to high-to]}]
           (->> state
                (filter (fn [[place {:keys [vals]}]]
                          (and (bot? place)
                               (= 2 (count vals)))))
                first)]
    (do     (println vals)
            (-> state
                (update place update :vals empty)
                (add-val low-to (apply min vals))
                (add-val high-to (apply max vals))))
    state))

(defn find-bot-vals
  [state x y]
  (->> state
       (filter (fn [[place {:keys [vals]}]]
                 (and (bot? place) (= #{x y} vals))))
       ffirst))

(defn find-bot
  [state x y]
  (loop [state state]
    (if-let [bot (find-bot-vals state x y)]
      bot
      (let [new-state (execute-step state)]
        (when-not (= new-state state)
          (recur new-state))))))


;;; Part 2

(defn execute-all
  [state]
  (loop [state state]
    (let [new-state (execute-step state)]
      (if (= new-state state)
        state
        (recur new-state)))))

(defn output-chips-values
  "Return values of outputs 0, 1, 2."
  [state]
  (select-keys (execute-all state) ["output 0" "output 1" "output 2"]))

(defn output-chips-products
  [state]
  (->> state
       output-chip-values
       (map (fn [[_ {:keys [vals]}]] (first vals)))
       (apply *)))

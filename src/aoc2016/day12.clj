(ns aoc2016.day12
  (:require [clojure.string :as str]))


;;; Part 1

;; You finally reach the top floor of this building: a garden with a slanted
;; glass ceiling. Looks like there are no more stars to be had.

;; While sitting on a nearby bench amidst some tiger lilies, you manage to
;; decrypt some of the files you extracted from the servers downstairs.

;; According to these documents, Easter Bunny HQ isn't just this building -
;; it's a collection of buildings in the nearby area. They're all connected by
;; a local monorail, and there's another building not far from here!
;; Unfortunately, being night, the monorail is currently not operating.

;; You remotely connect to the monorail control systems and discover that the
;; boot sequence expects a password. The password-checking logic (your puzzle
;; input) is easy to extract, but the code it uses is strange: it's assembunny
;; code designed for the new computer you just assembled. You'll have to
;; execute the code and get the password.

;; The assembunny code you've extracted operates on four registers (a, b, c,
;; and d) that start at 0 and can hold any integer. However, it seems to make
;; use of only a few instructions:

;; - cpy x y copies x (either an integer or the value of a register) into
;;   register y.

;; - inc x increases the value of register x by one.

;; - dec x decreases the value of register x by one.

;; - jnz x y jumps to an instruction y away (positive means forward; negative
;;   means backward), but only if x is not zero.

;; - The jnz instruction moves relative to itself: an offset of -1 would
;;   continue at the previous instruction, while an offset of 2 would skip
;;   over the next instruction.

;; For example:

;; cpy 41 a
;; inc a
;; inc a
;; dec a
;; jnz a 2
;; dec a

;; The above code would set register a to 41, increase its value by 2,
;; decrease its value by 1, and then skip the last dec a (because a is not
;; zero, so the jnz a 2 skips it), leaving register a at 42. When you move
;; past the last instruction, the program halts.

;; After executing the assembunny code in your puzzle input, what value is
;; left in register a?

(def registers #{"a" "b" "c" "d"})
(def init-state (apply hash-map (interleave registers (repeat 0))))

(defn try-parse-long
  [s]
  (if (contains? registers s)
    s
    (Long/parseLong s)))

(defn parse-instruction
  [s]
  (let [[cmd x y] (str/split s #" ")]
    (case cmd
      "cpy" [cmd (try-parse-long x) y]
      ("inc" "dec") [cmd x]
      "jnz" [cmd (try-parse-long x) (try-parse-long y)])))

(defn parse-input
  [input]
  (->> input
       str/split-lines
       (mapv parse-instruction)))

(defn get-value
  [state v]
  (if (contains? registers v)
    (get state v)
    v))

(defn execute-instruction
  [state pt [cmd x y]]
  (case cmd
    "cpy" [(assoc state y (get-value state x)) (inc pt)]
    "inc" [(update state x inc) (inc pt)]
    "dec" [(update state x dec) (inc pt)]
    "jnz" [state (if (not (zero? (get-value state x)))
                   (+ pt (get-value state y))
                   (inc pt))]))

(defn execute-instructions
  [state instructions]
  (let [n (count instructions)]
    (loop [state state
           pt 0]
      (if (<= 0 pt (dec n))
        (let [[new-state new-pt]
              (execute-instruction state pt (nth instructions pt))]
          (recur new-state new-pt))
        state))))

(defn a-value
  [state instructions]
  (get (execute-instructions state instructions) "a"))


;;; Part 2

;; As you head down the fire escape to the monorail, you notice it didn't
;; start; register c needs to be initialized to the position of the ignition
;; key.

;; If you instead initialize register c to be 1, what value is now left in
;; register a?

(def init-state-2
  (assoc init-state "c" 1))

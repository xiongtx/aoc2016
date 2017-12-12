(ns aoc2016.day4
  (:require [clojure.string :as str]))


;;; Part 1

;; Finally, you come across an information kiosk with a list of rooms. Of
;; course, the list is encrypted and full of decoy data, but the instructions
;; to decode the list are barely hidden nearby. Better remove the decoy data
;; first.

;; Each room consists of an encrypted name (lowercase letters separated by
;; dashes) followed by a dash, a sector ID, and a checksum in square brackets.

;; A room is real (not a decoy) if the checksum is the five most common
;; letters in the encrypted name, in order, with ties broken by
;; alphabetization. For example:

;; - aaaaa-bbbb-z-y-x-123[abxyz] is a real room because the most common
;;   letters are a (5), b (3), and then a tie between x, y, and z, which are
;;   listed alphabetically.

;; - a-b-c-d-e-f-g-h-987[abcde] is a real room because although the letters
;;   are all tied (1 of each), the first five are listed alphabetically.

;; - not-a-real-room-404[oarel] is a real room.

;; - totally-real-room-200[decoy] is not.

;; Of the real rooms from the list above, the sum of their sector IDs is 1514.

;; What is the sum of the sector IDs of the real rooms?

(def line-regex #"([a-z-]+[a-z])-([0-9]+)\[([a-z]+)\]")

(defn parse-line
  [line]
  (let [[_ nm id checksum] (re-find line-regex line)]
    [nm (Long/parseLong id) checksum]))

(defn parse-input
  [input]
  (->> (str/split-lines input)
       (map parse-line)))

(defn calculate-checksum
  [nm]
  (->> (str/replace nm "-" "")
       frequencies
       (sort-by first)
       (sort-by second >)
       (map first)
       (take 5)
       str/join))

(defn real-room?
  [[nm id checksum]]
  (= checksum (calculate-checksum nm)))

(defn sector-id-sum
  [rooms]
  (reduce (fn [score [nm id checksum :as room]]
            (if (real-room? room)
              (+ score id)
              score))
          0
          rooms))


;;; Part 2

;; With all the decoy data out of the way, it's time to decrypt this list and
;; get moving.

;; The room names are encrypted by a state-of-the-art shift cipher, which is
;; nearly unbreakable without the right software. However, the information
;; kiosk designers at Easter Bunny HQ were not expecting to deal with a master
;; cryptographer like yourself.

;; To decrypt a room name, rotate each letter forward through the alphabet a
;; number of times equal to the room's sector ID. A becomes B, B becomes C, Z
;; becomes A, and so on. Dashes become spaces.

;; For example, the real name for qzmt-zixmtkozy-ivhz-343 is very encrypted
;; name.

;; What is the sector ID of the room where North Pole objects are stored?

(defn char-shift
  [c k]
  (case c
    \- (if (odd? k) \space \-)
    \space (if (odd? k) \- \space)
    (char (+ (mod (- (+ (int c) k) 97) 26) 97))))

(defn decrypt-name
  [nm id]
  (->> nm
       (map #(char-shift % id))
       str/join))

(defn northpole-object-storage-id
  [rooms]
  (reduce (fn [_ [nm id _ :as room]]
            (when (= "northpole-object-storage"
                     (decrypt-name nm id))
              (reduced id)))
          rooms))

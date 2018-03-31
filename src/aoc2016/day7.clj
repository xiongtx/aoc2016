(ns aoc2016.day7
  (:require [clojure.string :as str]))


;;; Part 1

;; While snooping around the local network of EBHQ, you compile a list of IP
;; addresses (they're IPv7, of course; IPv6 is much too limited). You'd like
;; to figure out which IPs support TLS (transport-layer snooping).

;; An IP supports TLS if it has an Autonomous Bridge Bypass Annotation, or
;; ABBA. An ABBA is any four-character sequence which consists of a pair of
;; two different characters followed by the reverse of that pair, such as xyyx
;; or abba. However, the IP also must not have an ABBA within any hypernet
;; sequences, which are contained by square brackets.

;; For example:


;; - abba[mnop]qrst supports TLS (abba outside square brackets).

;; - abcd[bddb]xyyx does not support TLS (bddb is within square brackets, even
;;   though xyyx is outside square brackets).

;; - aaaa[qwer]tyui does not support TLS (aaaa is invalid; the interior
;;   characters must be different).

;; - ioxxoj[asdfgh]zxcvbn supports TLS (oxxo is outside square brackets, even
;;   though it's within a larger string).

;; How many IPs in your puzzle input support TLS?

(defn parse-ip [s]
  (let [re #"\[(.*?)\]"]
    {:hypernets (->> s
                     (re-seq re)
                     (map second))
     :supernets (str/split s re)}))

(defn parse-input
  [input]
  (->> input
       (str/split-lines)
       (map parse-ip)))

(defn abba?
  [s]
  (->> s
       (partition 4 1)
       (some (fn [[a b c d]]
               (and (not= a b)
                    (= a d)
                    (= b c))))
       boolean))

(defn supports-tls?
  [{:keys [hypernets supernets]}]
  (and (some abba? supernets)
       (not-any? abba? hypernets)))

(defn num-supports-tls
  [ips]
  (->> ips
       (map supports-tls?)
       (filter true?)
       count))


;;; Part 2

;; You would also like to know which IPs support SSL (super-secret listening).

;; An IP supports SSL if it has an Area-Broadcast Accessor, or ABA, anywhere
;; in the supernet sequences (outside any square bracketed sections), and a
;; corresponding Byte Allocation Block, or BAB, anywhere in the hypernet
;; sequences. An ABA is any three-character sequence which consists of the
;; same character twice with a different character between them, such as xyx
;; or aba. A corresponding BAB is the same characters but in reversed
;; positions: yxy and bab, respectively.

;; For example:

;; - aba[bab]xyz supports SSL (aba outside square brackets with corresponding
;;   bab within square brackets).

;; - xyx[xyx]xyx does not support SSL (xyx, but no corresponding yxy).

;; - aaa[kek]eke supports SSL (eke in supernet with corresponding kek in
;;   hypernet; the aaa sequence is not related, because the interior character
;;   must be different).

;; - zazbz[bzb]cdb supports SSL (zaz has no corresponding aza, but zbz has a
;;   corresponding bzb, even though zaz and zbz overlap).

;; How many IPs in your puzzle input support SSL?

(defn abas
  "Return all ABA sequences in string s."
  [s]
  (->> (partition 3 1 s)
       (filter (fn [[a b c]]
                 (and (not= a b) (= a c))))))

(defn bab?
  "Return true if string s contains BAB for given ABA."
  [s aba]
  (let [[a b] aba]
    (->> (partition 3 1 s)
         (some (fn [[x y z]]
                 (and (= b x z)
                      (= a y))))
         boolean)))

(defn supports-ssl?
  [{:keys [supernets hypernets]}]
  (-> (for [aba (set (mapcat abas supernets))
            s hypernets
            :when (bab? s aba)]
        true)
      not-empty
      boolean))

(defn num-supports-ssl
  [ips]
  (->> ips
       (map supports-ssl?)
       (filter true?)
       count))

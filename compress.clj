(ns compress
  (:require [clojure.string :as str]))


; Function that slices the file into a list of words
(defn slice
  [file_name]
  (str/split (slurp file_name) #" "))

; Function that removes the duplicates from the list of words and returns
; the list without duplicates
(defn check-duplicates
  [list]
  (loop [new-list [] current-index 0]
    ; checks if the current-index is less than the length of the list
    (if (< current-index (count list))
      ; checks if the current word is not in the new-list and ignore upper/lower case
      (if (not (some #(= (str/lower-case (nth list current-index)) (str/lower-case %)) new-list))
        ; if it's not in the new-list, it adds it to the new-list
        (recur (conj new-list (nth list current-index)) (inc current-index))
        ; if it's in the new-list, it doesn't add it to the new-list
        (recur new-list (inc current-index)))
      new-list)))


; Function that maps the words with their index
(defn map-words
  [list]
  (zipmap (range (count list)) list))

; Map that contains the words and their index
(def main-map-words (map-words (check-duplicates (slice "frequency.txt"))))

(spit "map.txt" (str main-map-words))

; Function that compresses a file with the help of a map called " main-map-words "
; that contains the words and their index.
; It replaces the number of the word in the file with the index in the map
; If the word is not in the map, it doesn't replace it with anything
; It returns a string in which the words are replaced with their index
(defn compress [file-name]
  (loop [new-string "" current-index 0]
    ; checks if the current-index is less than the length of the list
    (if (< current-index (count (slice file-name)))
      ; checks if the word is in the map
      (if (contains? main-map-words (nth (slice file-name) current-index))
        ; if it's in the map, it replaces the word with its index
        (recur (str new-string (get main-map-words (nth (slice file-name) current-index)) " ") (inc current-index))
        ; if it's not in the map, it doesn't replace it with anything
        (recur (str new-string (nth (slice file-name) current-index) " ") (inc current-index)))
      new-string)))

;; (println (compress "t1.txt"))


; Function that decompresses a file with the help of a map called
; "main-map-words" that contains the words and their index. It slices the file
; into a list of numbers and replaces the numbers of the list with the words from the map.
; This list is then converted into a string of words
; It returns a string of words
(defn decompress [file_name]
  (loop [list-number [] current-index 0]
    ; checks if the current-index is less than the length of the list
    (if (< current-index (count (slice file_name)))
      ; checks the number of the word in the list
      (if (number? (read-string (nth (slice file_name) current-index)))
        ; if it's a number, it adds it to the list
        (recur (conj list-number (nth (slice file_name) current-index)) (inc current-index))
        ; if it's not a number, it doesn't add it to the list
        (recur list-number (inc current-index)))
      list-number)))
  (println (decompress "t1_number.txt"))
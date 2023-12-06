(ns compress
  (:require [clojure.string :as str]))


; Function that slices the file into a list of words
(defn slice
  [file-name]
  (str/split (slurp file-name) #" "))

; Function that removes the duplicates from the list of words and returns
; the list without duplicates
(defn check-duplicates
  [list]
  (loop [new-list [] current-index 0]
    ; checks if the current-index is less than the length of the list
    (if (< current-index (count list))
      ; checks if the current word is not in the new-list and ignore upper/lower
      ; case
      (if (not (some #(= (str/lower-case (nth list current-index)) 
                         (str/lower-case %)) new-list))
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

; Function that compresses a file with the help of a map called "main-map-words"
; It uses regex to find the words in the file and replaces them with their index
; in the map. It replaces the number of the word in the file with the index in
; the map. If the word is not in the map, it doesn't replace it with anything
; It returns a string in which the words are replaced with their index.
; It also saves the punctuation marks.
(defn compress [file-name]
  (loop [new-string "" current-index 0]
    ; checks if the current-index is less than the length of the list
    (if (< current-index (count (slice file-name)))
      ; checks if the word is in the map
      (if (some #(= (str/lower-case (nth (slice file-name) current-index)) 
                    (str/lower-case %)) (vals main-map-words))
        ; if it's in the map, it replaces the word with the index of the word in
        ; the map
        (recur (str new-string 
                    (key (first 
                          (filter #(= (str/lower-case 
                                       (nth (slice file-name) current-index)) 
                                      (str/lower-case 
                                       (val %))) main-map-words))) " ")
               (inc current-index))
        ; if it's not in the map, it doesn't replace it with anything
        (recur (str new-string (nth (slice file-name) current-index) " ") 
               (inc current-index)))
      new-string)))



; https://stackoverflow.com/questions/5621279/in-clojure-how-can-i-convert-a-string-to-a-number
(defn parse-int [s]
  (Integer. (re-find  #"\d+" s)))

; Function that decompresses a file with the help of a map called
; "main-map-words". It uses regex to find the numbers in the file and
; replaces them with the word that has that index in the map.
(defn decompress [file_name]
  (loop [new-string "" current-index 0]
    ; checks if the current-index is less than the length of the list
    (if (< current-index (count (slice file_name)))
      ; checks if the word is a number
      (if (re-matches #"\d+" (nth (slice file_name) current-index))
        ; if it's a number, it replaces the number with the word that has that 
        ; index in the map
        (recur (str new-string 
                    (get main-map-words 
                         (parse-int (nth (slice file_name) current-index))) " ")
               (inc current-index))
        ; if it's not a number, it doesn't replace it with anything
        (recur (str new-string (nth (slice file_name) current-index) " ") 
               (inc current-index))) new-string)))
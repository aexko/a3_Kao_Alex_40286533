(ns compress
  (:require [clojure.string :as str]))

; Function that slices the file into a list of words with a space as a delimiter
(defn slice
  [file_name]
  (str/split (slurp file_name) #"\s"))


; Function that creates a map with the words as values and the index as keys.It
; also checks if the word is already in the map: if it is, it doesn't add it
; to the map
(defn mapping-words
  [list_words]
  (loop [index 0
         map {}]
    (if (< index (count list_words))
      (if (contains? map (nth list_words index))
        (recur (inc index) map)
        (recur (inc index) (assoc map (nth list_words index) index)))
      map)))


; map that contains the words and their index
(def main-map-words (mapping-words (slice "frequency.txt")))

; Function that compresses a file with the help of a map called "main-map-words"
; that contains the words and their index.
; It replaces the number of the word in the file with the index in the map
; If the word is not in the map, it doesn't replace it with anything
; It returns a string in which the words are replaced with their index
(defn compress
  [file_name]
  (loop [index 0
         string ""]
    (if (< index (count (slice file_name)))
      (if (contains? main-map-words (nth (slice file_name) index))
        (recur (inc index) (str string " " (main-map-words (nth (slice file_name) index))))
        (recur (inc index) (str string " " (nth (slice file_name) index))))
      string)))

; It's the reverse of compress. It takes a compressed file and decompresses it
; with the help of the map "main-map-words"
; It replaces the index of the word with the word itself with the help of
; the replace function in clojure.string library
; If the index is not in the map, it doesn't replace it with anything
; It returns a string in which the index are replaced with their words
(defn decompress
  [file_name]
    (loop [index 0
             string ""]
        (if (< index (count (slice file_name)))
        (if (contains? main-map-words (nth (slice file_name) index))
            (recur (inc index) (str string " " (str/replace (nth (slice file_name) index) (str index) (str (main-map-words (nth (slice file_name) index))))))
            (recur (inc index) (str string " " (nth (slice file_name) index))))
        string)))
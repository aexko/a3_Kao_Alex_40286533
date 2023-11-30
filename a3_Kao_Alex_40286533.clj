(ns a3_Kao_Alex_40286533
  (:require [clojure.pprint :as prt]
            [clojure.string :as str]
            [clojure.java.io :as io]))
  ; this is where you would also include/require the compress module


; Display the menu and ask the user for the option
(defn showMenu
  []
  (println "\n\n*** Compression Menu ***")
  (println "------------------\n")
  (println "1. Display list of files")
  (println "2. Display file contents")
  (println "3. Compress a file")
  (println "4. Uncompress a file")
  (println "5. Exit")
  (do
    (print "\nEnter an option? ")
    (flush)
    (read-line)))

; Get the list of files in the current folder
(def f (io/file (System/getProperty "user.dir")))
(def fs (file-seq f))

; Display all files in the current folder
(defn option1
  []
  ; print the list of files in a pretty way
  (prt/pprint (map #(.getName %) fs)))

; Function that checks if the file exists
(defn file-exists?
  [file_name]
  (.exists (io/file file_name)))

; Read and display the file contents (if the file exists). Java's File class can be used to 
; check for existence first. 
(defn option2
  []
  (print "\nPlease enter a file name => ")
  (flush)
  (let [file_name (read-line)]
    ; if the file exists, print the content of the file
    (if (file-exists? file_name)
      (println (slurp file_name))
      (println (str "### The file " file_name " does not exist. ###")))))


; Compress the (valid) file provided by the user. You will replace the println expression with code 
; that calls your compression function
(defn option3
  [] ;parm(s) can be provided here, if needed
  (print "\nPlease enter a file name => ")
  (flush)
  (let [file_name (read-line)]
    (println "now compress" file_name "with with the functions(s) you provide in compress.clj")))



; Decompress the (valid) file provided by the user. You will replace the println expression with code 
; that calls your decompression function
(defn option4
  [] ;parm(s) can be provided here, if needed
  (print "\nPlease enter a file name => ")
  (flush)
  (let [file_name (read-line)]
    (println "now decompress" file_name "with with the functions(s) you provide in compress.clj")))


; If the menu selection is valid, call the relevant function to 
; process the selection
(defn processOption
  [option] ; other parm(s) can be provided here, if needed
  (if (= option "1")
    (option1)
    (if (= option "2")
      (option2)
      (if (= option "3")
        (option3)  ; other args(s) can be passed here, if needed
        (if (= option "4")
          (option4)   ; other args(s) can be passed here, if needed
          (println "Invalid Option, please try again"))))))


; Display the menu and get a menu item selection. Process the
; selection and then loop again to get the next menu selection
(defn menu
  [] ; parm(s) can be provided here, if needed
  (let [option (str/trim (showMenu))]
    (if (= option "5")
      (println "\nGood Bye\n")
      (do
        (processOption option)
        (recur)))))   ; other args(s) can be passed here, if needed

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
; It replaces the index of the word with the word itself
; If the index is not in the map, it doesn't replace it with anything
; It returns a string in which the index are replaced with their words
(defn decompress
  [file_name]
  (loop [index 0
         string ""]
    (if (< index (count (slice "t1.txt.ct")))
      (if (contains? main-map-words (nth (slice "t1.txt.ct") index))
        (recur (inc index) (str string " " (key (get main-map-words (nth (slice "t1.txt.ct") index)))))
        (recur (inc index) (str string " " (nth (slice "t1.txt.ct") index))))
      string)))

; ------------------------------
; Run the program. You might want to prepare the data required for the mapping operations
; before you display the menu. You don't have to do this but it might make some things easier

;; (menu) ; other args(s) can be passed here, if needed


;; playground

; function that writes a map in a file called test.txt

(spit "t1.txt.ct" (compress "t1.txt"))
(spit "t1.txt.dt" (decompress "t1.txt.ct"))
;; (spit "test.txt" (decompress "t1.txt"))
;; (print (compress "t1.txt"))



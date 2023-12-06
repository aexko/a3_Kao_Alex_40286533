(ns menu
  (:require [clojure.pprint :as prt]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [compress :as compress]))

; ------------------------------
; Note: ONLY t1.txt has been tested, punctuations are not handled, neither are
; numbers.

; Display the menu and ask the user for the option
(defn showMenu []
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
(defn option1 []
  ; Print the list of files in a pretty way
  (prt/pprint (map #(.getName %) fs)))

; Function that checks if the file exists
(defn file-exists? [file-name]
  (.exists (io/file file-name)))

; Read and display the file contents (if the file exists). Java's File class can
; be used to check for existence first. 
(defn option2 []
  (print "\nPlease enter a file name => ")
  (flush)
  (let [file-name (read-line)]
    ; if the file exists, print the content of the file
    (if (file-exists? file-name)
      (println (slurp file-name))
      (println (str "### The file " file-name " does not exist. ###")))))


; Compress the (valid) file provided by the user. You will replace the println
; expression with code that calls your compression function
(defn option3 []
  (print "\nPlease enter a file name => ")
  (flush)
  (let [file-name (read-line)]
    (if (file-exists? file-name)
      (spit (str file-name ".ct") (compress/compress file-name))
      (println (str "### The file " file-name " does not exist. ###")))))



; Decompress the (valid) file provided by the user. You will replace the println
; expression with code that calls your decompression function
(defn option4 []
  (print "\nPlease enter a file name => ")
  (flush)
  (let [file-name (read-line)]
    (if (file-exists? file-name)
      (spit (str file-name ".dt") (compress/decompress file-name))
      (println (str "### The file " file-name " does not exist. ###")))))


; If the menu selection is valid, call the relevant function to 
; process the selection
(defn processOption [option]
  (if (= option "1")
    (option1)
    (if (= option "2")
      (option2)
      (if (= option "3")
        (option3)
        (if (= option "4")
          (option4)
          (println "Invalid Option, please try again"))))))


; Display the menu and get a menu item selection. Process the
; selection and then loop again to get the next menu selection
(defn menu []
  (let [option (str/trim (showMenu))]
    (if (= option "5")
      (println "\nGood Bye\n")
      (do
        (processOption option)
        (recur)))))

; ------------------------------
; Run the program. You might want to prepare the data required for the mapping 
; operations before you display the menu. You don't have to do this but it might
; make some things easier

(menu)




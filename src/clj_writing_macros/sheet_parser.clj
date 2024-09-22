(ns clj-writing-macros.sheet-parser
  (:require [clojure.java.io :as io]
            [clojure.walk :refer (keywordize-keys)])
  (:import
   (org.apache.poi.xssf.usermodel XSSFWorkbook)))


(defn- get-cell-value [cell]
  (let [cell-type (.getCellType cell)]
    (case (.name cell-type)
      "STRING" (.getStringCellValue cell)
      "NUMERIC" (num (.getNumericCellValue cell))
      nil)))

(defn- parse-data-row [data-row attr-keys]
  (let [cells (iterator-seq  (.cellIterator data-row))
        cell-values (vec (map get-cell-value cells))]
    (keywordize-keys (reduce-kv
                      (fn [acc idx cell-value]
                        (assoc acc (nth attr-keys idx "unknown") cell-value))
                      {}
                      cell-values))))

(defn- parse-attr-keys [keys-row]
  (let [special-keys {0 "name" 1 "employee_id" 2 "period_start_date" 3 "period_end_date"}
        cells (iterator-seq (.cellIterator keys-row))
        get-key (fn get-key [idx cell] (or (get special-keys idx) (.getStringCellValue cell)))]
    (map-indexed get-key cells)))


(defn parse-xslx [file]
  (with-open [xin (io/input-stream file)
              wb  (XSSFWorkbook. xin)]
    (let [sheet (.getSheetAt wb 0)
          rows (iterator-seq (.iterator sheet))
          attr-keys (parse-attr-keys (first rows))
          data-rows (drop 1 rows)]
      (map #(parse-data-row %1 attr-keys) data-rows))))


    ;; (let [sheet (.getSheetAt wb 0)
    ;;       rows (iterator-seq (.iterator sheet))
    ;;       attr-keys (parse-attr-keys (nth (take 1 rows) 0))
    ;;       data-rows (drop 3 rows)]
    ;;   (map #(parse-data-row %1 attr-keys) data-rows))))

;; (println (parse-xslx "resources/sample.xlsx"))

;; (spit "tmp/test.txt" (vec (parse-xslx "resources/sample.xlsx")))
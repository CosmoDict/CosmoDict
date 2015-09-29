SELECT T4.priority AS priority
     , IF(T3.tr_id IS NULL,0,1) AS mask_bit
    FROM LANG T4
        LEFT OUTER JOIN
(
SELECT 
    T1.lang_id AS lang_id,
    MIN(T2.tr_id) AS tr_id
FROM
    LANG T1
        INNER JOIN
    TRANSLATION T2 
        ON (T2.lang_id = T1.lang_id)
WHERE
    T2.def_id = ?
GROUP BY T1.lang_id
) T3
 ON (T3.lang_id = T4.lang_id)
ORDER BY T4.priority ASC
    
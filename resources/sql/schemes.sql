-- :name get-schemas :? :*
SELECT * FROM details_schema;

-- :name get-details-by-ids :? :*
SELECT * FROM detail_item WHERE id IN (:v*:ids);

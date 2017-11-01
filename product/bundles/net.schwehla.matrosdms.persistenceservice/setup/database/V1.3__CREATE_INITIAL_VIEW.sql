create or replace view VW_CONTEXT as select c.* ,
 count (i.context_id) as sum from context c left join item i on i.context_id = c.CONTEXT_ID 
 where c.datearchived is null and i.datearchived is null group by c.CONTEXT_ID order by c.CONTEXT_ID;

 
create or replace view vw_search as
SELECT DISTINCT
c.CONTEXT_ID as CONTEXT_ID
, c.NAME as CON_NAME
, c.UUID as CON_UUID
, c.STAGE as CON_STAGE
, I.ITEM_ID
, I.NAME as ITEM_NAME
, I.UUID as ITEM_UUID
, c.DATEARCHIVED as CON_DATEARCHIVED
, I.DATEARCHIVED as ITEM_DATEARCHIVED
, I.DATEARCHIVED is not null or c.DATEARCHIVED is not null as ELEMENT_ARCHIVED
,I.STORE_STORE_ID
,I.STORAGEITEMIDENTIFIER
FROM CONTEXT C
JOIN ITEM I
ON I.CONTEXT_ID = C.CONTEXT_ID
LEFT OUTER JOIN ATTRIBUTE A
ON I.ITEM_ID = A.ITEM_ID
ORDER BY CONTEXT_ID, CON_NAME ;

-- VW_MASTERDATA_UUID ------------------------------------------------------

create or replace View VW_MASTERDATA_UUID as 
select x.TAB_TYPE, x.ID, x.UUID from (
  SELECT 'ATTRIBUTETYPE'  TAB_TYPE, ATTRIBUTETYPE_ID as ID,  UUID  
  FROM ATTRIBUTETYPE 
  union
  SELECT 'KATEGORY'  TAB_TYPE, KATEGORY_ID as ID,  UUID  
  FROM KATEGORY
  union
  SELECT 'STORE'  TAB_TYPE, STORE_ID as ID,  UUID  
  FROM STORE
  union
  SELECT 'USER'  TAB_TYPE, USER_ID as ID,  UUID  
  FROM USER ) x
order by 1,2;


-- VW_TRANSACTIONDATA_UUID ------------------------------------------------------
create or replace View VW_TRANSACTIONDATA_UUID as 
  select x.TAB_TYPE , x.ID, x.UUID from (
  SELECT 'CONTEXT' as TAB_TYPE, CONTEXT_ID as ID,  UUID  
  FROM CONTEXT
  union 
  SELECT 'EVENT' as TAB_TYPE, EVENT_ID as ID,  UUID  
  FROM EVENT
  union
  SELECT 'ITEM' as TAB_TYPE , ITEM_ID as ID,  UUID  
  FROM ITEM  ) x 
 order by 1,2;

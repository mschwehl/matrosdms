create or replace view vw_context as

select c.* , count (ic.container_id) as sum from context c left join storableinfoitemcontainer ic on ic.container_id = c.id group by c.id order by c.id ;

--


--create or replace view vw_suche as

SELECT

c.CONTEXT_ID as CONTEXT_ID
, c.NAME as CON_NAME
, c.DATEARCHIVED as CON_DATEARCHIVED 
, I.ITEM_ID
, I.NAME as ITEM_NAME
, I.DATEARCHIVED as ITEM_DATEARCHIVED
, I.DATEARCHIVED is not null or c.DATEARCHIVED is not null as ELEMENT_ARCHIVED
, count (a.ATTRIBUTE_ID) as ATTRIBUTE

FROM CONTEXT C
LEFT OUTER JOIN ITEM I
    ON I.CONTEXT_ID = C.CONTEXT_ID
LEFT OUTER JOIN ATTRIBUTE A
    ON I.ITEM_ID = A.ITEM_ID     

group by C.CONTEXT_ID, CON_NAME, CON_DATEARCHIVED , i.ITEM_ID, ITEM_NAME, ITEM_DATEARCHIVED, ELEMENT_ARCHIVED

ORDER BY CONTEXT_ID, ITEM_NAME;

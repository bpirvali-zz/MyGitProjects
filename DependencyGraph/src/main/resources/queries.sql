WITH dep_tree(ID, installed, obj, dep, route, LEVEL) AS ( 
    SELECT id, installed, obj, dependent_on, CAST(CONCAT(obj, ' --> ', dependent_on) AS VARCHAR(128)) , 1 
    FROM obj_dependency 
    WHERE obj='DNS' 
    UNION ALL 
    SELECT a.id, a.installed, a.obj, b.dependent_on, CAST(CONCAT(a.route, ' --> ', b.dependent_on) AS VARCHAR(128)) , a.LEVEL + 1 
    FROM dep_tree a INNER JOIN obj_dependency b ON b.obj = a.dep
) 
SELECT * from dep_tree
where dep!=''
AND dep in (SELECT obj FROM obj_dependency WHERE dependent_on='');
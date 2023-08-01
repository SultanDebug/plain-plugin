<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">

<#if enableCache>
    <!-- 开启二级缓存 -->
    <cache type="org.mybatis.caches.ehcache.LoggingEhcache"/>

</#if>
<#if baseResultMap>
    <!-- 通用查询映射结果 -->
    <resultMap id="baseResultMap" type="${package.Entity}.${entity}">
<#list table.fields as field>
<#if field.keyFlag><#--生成主键排在第一位-->
        <id column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
<#list table.commonFields as field><#--生成公共字段 -->
    <result column="${field.name}" property="${field.propertyName}" />
</#list>
<#list table.fields as field>
<#if !field.keyFlag><#--生成普通字段 -->
        <result column="${field.name}" property="${field.propertyName}" />
</#if>
</#list>
    </resultMap>

</#if>
<#if baseColumnList>
    <!-- 通用查询结果列 -->
    <sql id="baseColumnSql">
<#list table.fields as field>
        t.${field.name}<#if field_has_next>,</#if>
</#list>
        <#--${table.fieldNames}-->
    </sql>

</#if>

    <!-- 增 -->
    <insert id="save" parameterType="${package.Entity}.${entity}" useGeneratedKeys="true" keyProperty="id">
        insert into `${table.name}`
        (
        <#list table.fields as field>
            <#if field.name != "id" && field.name != "version">`${field.name}`<#if field_has_next>,</#if></#if>
        </#list>
        )
        values
        (
        <#list table.fields as field>
            <#if field.name != "id" && field.name != "version"><#noparse>#{</#noparse>${field.propertyName}}<#if field_has_next>,</#if></#if>
        </#list>
        )
    </insert>

    <!-- 删 -->
    <update id="delete">
        update `${table.name}` t
        <set>
            t.deleted = 1
        </set>
        where
        id = <#noparse>#{</#noparse>id}
    </update>

    <!-- 改 -->
    <update id="update" parameterType="${package.Entity}.${entity}">
        update `${table.name}` t
        <set>
            <#list table.fields as field>
                <#if field.name != "id" && field.name != "version"><if test="${field.propertyName} != null">`${field.name}` = <#noparse>#{</#noparse>${field.propertyName}}<#if field_has_next>,</#if></if></#if>
            </#list>
        </set>
        where id = <#noparse>#{</#noparse>id}
    </update>

    <!-- 查 -->
    <select id="get" resultMap="baseResultMap">
        select
        <include refid="baseColumnSql"/>
        from `${table.name}` t
        where
        t.deleted = 0
        and id = <#noparse>#{</#noparse>id}
    </select>


</mapper>

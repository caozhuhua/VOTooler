package com.eray.excel.vo
{
	import flash.utils.ByteArray;
<#list TypeList as field>
	import com.eray.excel.vo.${field.includeFileName}
</#list>
	public class ${ClassName}
	{
		public function ${ClassName}()
		{
		
		}
<#list FieldList as field>
	<#if field.count != "" && field.type!="String">
		private var _${field.fieldName}:Array;
		/**
		 * ${field.comment}
		 */
		public function get ${field.fieldName}():Array
		{
			return _${field.fieldName};
		}

		public function set ${field.fieldName}(value:Array):void
		{
			_${field.fieldName} = value;
		}
		<#else>
		private var _${field.fieldName}:${field.type};
		/**
		 * ${field.comment}
		 */
		public function get ${field.fieldName}():${field.type}
		{
			return _${field.fieldName};
		}

		public function set ${field.fieldName}(value:${field.type}):void
		{
			_${field.fieldName} = value;
		}
	</#if>
</#list>
		
		public static function parse(ba:ByteArray):${ClassName}{
			var i:int,j:int,__type:int,__listNum:int;
			var vo:${ClassName} = new ${ClassName}();
<#list FieldList as field>
	<#if field.type == "int">
			vo.${field.fieldName} =  ba.readUnsignedInt();
		<#elseif field.type == "Number">
			vo.${field.fieldName} =  ba.getLong();
		<#elseif field.type == "String">
			vo.${field.fieldName} =  Utilities.UnicodeToString(ba,${field.count}/2)
		<#elseif field.type == "Object">
			<#list field.voLogic as fd>
			__type = ba.readUnsignedInt();
			if(__type==${fd.voType}){
				<#if fd.num == "1">
				vo.${field.fieldName} = ${fd.objType}.parse(ba);
				<#else>
				__listNum = ba.readUnsignedInt();
				vo.${field.fieldName} = [];
				for(j=0;j<__listNum;++j){
					vo.${field.fieldName}.push(${fd.objType}.parse(ba));
				}
				</#if>
			}
			</#list>
		<#else>
		<#if field.count != "">
			for(i=0;i<${field.count};++i){
				this._${field.fieldName} = [];
				this._${field.fieldName}.push(${field.type}.parse(ba));
			}
		</#if>
	</#if>
</#list>
			return vo;
		}
	
	}
}
package com.eray.base.net.socket.vo
{
	import flash.utils.ByteArray;
	import com.eray.base.utils.Utilities;
<#list TypeList as field>
	import com.eray.base.net.socket.vo.${field.includeFileName}
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
			<#if field.countType == "1">
			vo.${field.fieldName} =  Utilities.UnicodeToString(ba,vo.${field.count}/2)
			<#else>
			o.${field.fieldName} =  Utilities.UnicodeToString(ba,${field.count}/2)
			</#if>
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
			vo.${field.fieldName} = [];
			<#if field.countType == "1">
			for(i=0;i<vo.${field.count};++i){
			<#else>
			for(i=0;i<${field.count};++i){
			</#if>
				vo.${field.fieldName}.push(${field.type}.parse(ba));
			}
		<#else>
			vo.${field.fieldName} = ${field.type}.parse(ba);
		</#if>
	</#if>
</#list>
			return vo;
		}
		public function toByteArray():ByteArray{
			var i:int;
			var ba:ByteArray = new ByteArray();
<#list FieldList as field>
	<#if field.type == "int">
			ba.writeUnsignedInt(this.${field.fieldName});
	<#elseif field.type == "Number">
			ba.putLong(this.${field.fieldName});
	<#elseif field.type == "String">
			Utilities.StringToUnicode(ba, this.${field.fieldName}, ${field.count});
	<#elseif field.type == "Object">
		<#list field.voLogic as fd>
		</#list>
	<#else>
		<#if field.count != "">
			<#if field.countType == "1">
			ba.writeUnsignedInt(${field.count});
			<#else>
			</#if>
			for(i=0;i<${field.count};++i){
				ba.writeBytes(${field.fieldName}[i].toByteArray());
			}
		</#if>
	</#if>
</#list>
			return ba;
		}
	}
}
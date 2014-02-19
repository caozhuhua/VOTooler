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
	<#elseif field.type == "Object" && field.count == "">
		private var _${field.fieldName}Type:int;
		public function get ${field.fieldName}Type():int
		{
			return _${field.fieldName}Type;
		}

		public function set ${field.fieldName}Type(value:int):void
		{
			_${field.fieldName}Type = value;
		}
		private var _${field.fieldName}Num:int;
		public function get ${field.fieldName}Num():int
		{
			return _${field.fieldName}Num;
		}

		public function set ${field.fieldName}Num(value:int):void
		{
			_${field.fieldName}Num = value;
		}
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
			var i:int,j:int;
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
			vo.${field.fieldName} =  Utilities.UnicodeToString(ba,${field.count}/2)
			</#if>
		<#elseif field.type == "Object">
			vo.${field.fieldName}Type = ba.readUnsignedInt();
			<#list field.voLogic as fd>
			if(vo.${field.fieldName}Type==${fd.voType}){
				<#if fd.num == "1">
				vo.${field.fieldName}Num = 1;
				vo.${field.fieldName} = ${fd.objType}.parse(ba);
				<#else>
				vo.${field.fieldName}Num = ba.readUnsignedInt();
				vo.${field.fieldName} = [];
				for(j=0;j<vo.${field.fieldName}Num;++j){
					vo.${field.fieldName}.push(${fd.objType}.parse(ba));
				}
				</#if>
			}
			</#list>
		<#else>
		<#if field.count != "">
			vo.${field.fieldName} = [];
			<#if field.countType == "1">
			//dynamic length
			for(i=0;i<vo.${field.count};++i){
			<#else>
			//fix length
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
			var i:int,j:int;
			var ba:ByteArray = new ByteArray();
<#list FieldList as field>
	<#if field.type == "int">
			ba.writeUnsignedInt(this.${field.fieldName});
	<#elseif field.type == "Number">
			ba.putLong(this.${field.fieldName});
	<#elseif field.type == "String">
			Utilities.StringToUnicode(ba, this.${field.fieldName}, ${field.count});
	<#elseif field.type == "Object">
		<#if field.count == "">
			ba.writeUnsignedInt(${field.fieldName}Type);
			<#list field.voLogic as fd>
			if(${field.fieldName}Type==${fd.voType}){
				<#if fd.num == "1">
				ba.writeBytes(${fd.objType}.toByteArray());
				<#else>
				ba.writeUnsignedInt(${field.fieldName}Num);
				for(j=0;j<${field.fieldName}Num;++j){
					ba.writeBytes(${fd.objType}.toByteArray());
				}
				</#if>
			}
			</#list>
		<#else>
			ba.writeBytes(${field.fieldName}.toByteArray());
		</#if>
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
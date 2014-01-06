#ifndef ERAY_OMG_${ClassName}_h
#define ERAY_OMG_${ClassName}_h
#include <iostream>
<#list TypeList as field>
#include "${field.includeFileName}.h"
</#list>
namespace ERAY{
    namespace OMG{
		struct ${ClassName}:public ErayBaseVO{
		<#if ICV.classInitItem != "">
			${ClassName}(){
				${ICV.classInitItem} =  ${ICV.classInitItemValue};
			}
		</#if>
			/**
			* Created by ErayVOTooler
		   	*/
		<#list FieldList as field>
			/**
			 * ${field.comment}
			 */
			<#if field.count == "" >
			${field.type} ${field.fieldName};
			<#else>
			${field.type}* ${field.fieldName};
			</#if>
		</#list>
			
			void parse(char* buffer){
			<#list FieldList as field>
				<#if field.count != "" >
				{
					int size = sizeof(${field.type})*${field.count};
					${field.fieldName} = new ${field.type}[${field.count}];
					memcpy(${field.fieldName},buffer,size);
					buffer+=size;
				}
				<#else>
				{
					int size = sizeof(${field.type});
					memcpy(&${field.fieldName},buffer,size);
					buffer+=size;
				}
				</#if>
			</#list>
			}
		};
    }
}
#endif // __${ClassName}_H__
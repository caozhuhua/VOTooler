#ifndef ERAY_OMG_MESSAGE_HEAD_h
#define ERAY_OMG_MESSAGE_HEAD_h
#include <iostream>
#include "EraySocketManager.h"
<#list FieldList as field>
const int ${field.messageIDStr} = ${field.messageId};
</#list>

namespace ERAY{
    namespace OMG{
	class ${ClassName}{
	<#list FieldList as field>
	      virtual void ${field.messageIDStr}(ErayBaseMessageVO* data)
	      {
		 		${field.actionHandler}((${field.messageVO}*)data);
	      }
	      virtual void ${field.actionHandler}(ErayLoginVO* data) = 0;
	</#list>
		  /**
		   * Created by ErayVOTooler
		  */
	      void registerMessageCallBack(){
	      <#list FieldList as field>
			EraySocketManager::getInstance()->registerMessageCallBack(${field.messageIDStr}, (MSG_OBSERVER)&${ClassName}::${field.messageIDStr}, this);
		  </#list>
	      }
		}
    }
}
#endif // __ERAY_OMG_MESSAGE_HEAD_h__
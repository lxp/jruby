/*
 **** BEGIN LICENSE BLOCK *****
 * Version: CPL 1.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Common Public
 * License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/cpl-v10.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Copyright (C) 2011 Charles O Nutter <headius@headius.com>
 * 
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the CPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the CPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/
package org.jruby.internal.runtime.methods;

import org.jruby.RubyClass;
import org.jruby.RubyModule;
import org.jruby.internal.runtime.methods.JavaMethod.JavaMethodZero;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.Visibility;
import org.jruby.runtime.builtin.IRubyObject;

/**
 * A method type for attribute writers (as created by attr_writer or attr_accessor).
 */
public class AttrReaderMethod extends JavaMethodZero {
    private RubyClass.VariableAccessor accessor = RubyClass.VariableAccessor.DUMMY_ACCESSOR;

    public AttrReaderMethod(RubyModule implementationClass, Visibility visibility, CallConfiguration callConfig, String variableName) {
        super(implementationClass, visibility, callConfig, variableName);
    }

    public IRubyObject call(ThreadContext context, IRubyObject self, RubyModule clazz, String name) {
        IRubyObject variable = (IRubyObject) verifyAccessor(self.getMetaClass().getRealClass()).get(self);
        return variable == null ? context.nil : variable;
    }
    
    public String getVariableName() {
        return name;
    }

    private RubyClass.VariableAccessor verifyAccessor(RubyClass cls) {
        RubyClass.VariableAccessor localAccessor = accessor;
        if (localAccessor.getClassId() != cls.id) {
            localAccessor = cls.getVariableAccessorForRead(name);
            accessor = localAccessor;
        }
        return localAccessor;
    }
}

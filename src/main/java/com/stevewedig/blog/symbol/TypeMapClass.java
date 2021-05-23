package com.stevewedig.blog.symbol;

import com.google.common.collect.Maps;
import com.stevewedig.blog.errors.NotContained;
import com.stevewedig.blog.util.CastLib;
import com.stevewedig.blog.value_objects.EntityMixin;

import java.util.HashMap;
import java.util.Map;

class TypeMapClass extends EntityMixin implements TypeMap
{

   // ===========================================================================
   // state
   // ===========================================================================

   private final Map<Class<?>, Object> state;

   public TypeMapClass()
   {
      this.state = new HashMap<>();
   }

   public TypeMapClass(Map<Class<?>, Object> state)
   {
      this.state = Maps.newHashMap(state); // defensive copy
   }

   // ===========================================================================
   // constructor
   // ===========================================================================

   @Override
   public Map<Class<?>, Object> stateCopy()
   {
      return Maps.newHashMap(state);
   }

   @Override
   protected Object[] fields()
   {
      Object[] fields = new Object[state.size() * 2];
      int i = 0;
      for (Map.Entry<Class<?>, Object> entry : state.entrySet())
      {
         fields[i++] = entry.getKey();
         fields[i++] = entry.getValue();
      }
      return fields;
   }

   // ===========================================================================
   // core behavior
   // ===========================================================================

   @Override
   public <T> TypeMap put(Class<T> type, T value)
   {
      state.put(type, value);
      return this; // fluent
   }

   @Override
   public <T> T get(Class<T> type)
   {

      if (!containsKey(type))
         throw new NotContained("type = %s", type);

      // this is type safe as long the entry was added via ClassMap.put()
      return CastLib.get(state, type);
   }

   // ===========================================================================
   // other map behavior
   // ===========================================================================

   @Override
   public boolean containsKey(Class<?> type)
   {
      return state.containsKey(type);
   }

   @Override
   public void remove(Class<Integer> type)
   {
      state.remove(type);
   }

   @Override
   public boolean isEmpty()
   {
      return state.isEmpty();
   }

   @Override
   public void clear()
   {
      state.clear();
   }

   @Override
   public int size()
   {
      return state.size();
   }

}

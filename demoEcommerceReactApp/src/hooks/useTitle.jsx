import React, { useEffect } from 'react'

export const useTitle = (title) => {
  useEffect(()=>{
    document.title = `${title} - KT Store`
  }, [title])
  return null;
}

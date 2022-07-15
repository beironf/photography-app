import {
  useCallback, useEffect, useLayoutEffect, useRef,
} from 'react';

export const ARROW_LEFT = 'ArrowLeft';
export const ARROW_UP = 'ArrowUp';
export const ARROW_RIGHT = 'ArrowRight';
export const ARROW_DOWN = 'ArrowDown';

export const useKeyPress = (
  keys: string[],
  callback: (_: KeyboardEvent) => void,
  node: Node = null,
): void => {
  // avoid having to put the callback inside the dependency array
  const callbackRef = useRef(callback);
  useLayoutEffect(() => {
    callbackRef.current = callback;
  });

  const handleKeyPress = useCallback(
    (event: KeyboardEvent) => {
      if (keys.some((key) => event.key === key)) {
        callbackRef.current(event);
      }
    },
    [keys],
  );

  useEffect(() => {
    const targetNode = node ?? document;
    targetNode.addEventListener('keydown', handleKeyPress);

    return () => targetNode
        && targetNode.removeEventListener('keydown', handleKeyPress);
  }, [handleKeyPress, node]);
};

import React from 'react';
import styles from './List.module.scss';
import RemovableListItem from '../RemovableListItem';

type ListProps = {
    /** An array of items, each with a description and a unique key. */
    items: {text: string, key: string}[],
    /** A callback function that handles item removal */
    removeItem: (key:string) => void
}

const List: React.FC<ListProps> = ({items, removeItem}: ListProps) => {
    return (
        <ul className={styles.list}>
            {items.map(item =>  
                <RemovableListItem text={item.text} key={item.key} id={item.key} removeItem={removeItem}/>   
            )}
        </ul>
    )
  }

  export default List;
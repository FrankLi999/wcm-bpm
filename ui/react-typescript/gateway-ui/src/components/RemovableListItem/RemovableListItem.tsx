import React, {useState} from 'react';
import styles from './RemovableListItem.module.scss';

type RemovableListItemProps = {
    /** The item's text */
    text: string,
    id: string,
    /** A callback function for the "X" click event */
    removeItem: (id: string) => void
}


const RemovableListItem : React.FC<RemovableListItemProps> = ({text, id, removeItem} : RemovableListItemProps) => {
    const [isCompleted, setIsCompleted] = useState(false);
    return(
        <li className={styles.listItem}>
            <span data-iscompleted={isCompleted} className={styles.text}  onClick={() => setIsCompleted(!isCompleted)}>{text}</span>
            <button className={styles.delete} onClick={() => removeItem(id)}>X</button>
        </li>
    )
}

export default RemovableListItem;
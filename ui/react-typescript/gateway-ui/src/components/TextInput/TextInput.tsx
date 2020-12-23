import React, {InputHTMLAttributes} from 'react';
import styles from './TextInput.module.scss';

type TextInputProps = InputHTMLAttributes<HTMLInputElement>;

const TextInput: React.FC<TextInputProps> = (props) => {
    return <input {...props} className={styles.input}></input>
}

export default TextInput;
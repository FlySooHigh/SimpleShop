package org.flysoohigh.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pair<U, V> {
    private U user;
    private V message;
}

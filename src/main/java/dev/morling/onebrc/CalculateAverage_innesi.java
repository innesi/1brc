/*
 *  Copyright 2023 The original authors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package dev.morling.onebrc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.DoubleSummaryStatistics;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

public class CalculateAverage_innesi {

    private final static File FILE = new File("measurements.txt");

    private class Temperature extends DoubleSummaryStatistics {
        @Override
        public String toString() {
            return String.format(Locale.US, "%.1f/%.1f/%.1f", this.getMin(),
                    this.getAverage(), this.getMax());
        }
    }

    public static void main(String[] args) {
        try {
            Map<String, Temperature> measures = Files.lines(FILE.toPath())
                    .parallel()
                    .map(line -> line.split(";"))
                    .collect(Collectors.toMap(
                            ct -> ct[0],
                            ct -> {
                                Temperature temperature = new CalculateAverage_innesi().new Temperature();
                                temperature.accept(Double.parseDouble(ct[1]));
                                return temperature;
                            },
                            (temp1, temp2) -> {
                                temp1.combine(temp2);
                                return temp1;
                            },
                            ConcurrentSkipListMap::new));

            System.out.println(measures);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

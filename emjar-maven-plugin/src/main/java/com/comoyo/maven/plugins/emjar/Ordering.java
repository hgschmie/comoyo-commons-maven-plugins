/**
 * Copyright (C) 2014 Telenor Digital AS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.comoyo.maven.plugins.emjar;

public class Ordering
{
    private String prefer;
    private String over;

    public Ordering() {}

    public Ordering(String prefer, String over)
    {
        this.prefer = prefer;
        this.over = over;
    }

    public String getPrefer()
    {
        return prefer;
    }

    public String getOver()
    {
        return over;
    }
}

package com.example.camc.view.main_screen

import weka.classifiers.AbstractClassifier
import weka.core.Capabilities
import weka.core.Instance
import weka.core.Instances
import weka.core.RevisionUtils


class WekaWrapper

    : AbstractClassifier() {
    /**
     * Returns only the toString() method.
     *
     * @return a string describing the classifier
     */
    fun globalInfo(): String {
        return toString()
    }

    /**
     * Returns the capabilities of this classifier.
     *
     * @return the capabilities
     */
    override fun getCapabilities(): Capabilities {
        val result = Capabilities(this)

        result.enable(Capabilities.Capability.NOMINAL_ATTRIBUTES)
        result.enable(Capabilities.Capability.NUMERIC_ATTRIBUTES)
        result.enable(Capabilities.Capability.DATE_ATTRIBUTES)
        result.enable(Capabilities.Capability.MISSING_VALUES)
        result.enable(Capabilities.Capability.NOMINAL_CLASS)
        result.enable(Capabilities.Capability.MISSING_CLASS_VALUES)


        result.minimumNumberInstances = 0

        return result
    }

    /**
     * only checks the data against its capabilities.
     *
     * @param i the training data
     */
    @Throws(Exception::class)
    override fun buildClassifier(i: Instances) {
        // can classifier handle the data?
        capabilities.testWithFail(i)
    }

    /**
     * Classifies the given instance.
     *
     * @param i the instance to classify
     * @return the classification result
     */
    @Throws(Exception::class)
    override fun classifyInstance(i: Instance): Double {
        val s = arrayOfNulls<Any>(i.numAttributes())

        for (j in s.indices) {
            if (!i.isMissing(j)) {
                if (i.attribute(j).isNominal) s[j] = java.lang.String(i.stringValue(j)) as String
                else if (i.attribute(j).isNumeric) s[j] = i.value(j)
            }
        }


        // set class value to missing
        s[i.classIndex()] = null

        return WekaClassifier.classify(s)
    }

    /**
     * Returns the revision string.
     *
     * @return        the revision
     */
    override fun getRevision(): String {
        return RevisionUtils.extract("1.0")
    }

    /**
     * Returns only the classnames and what classifier it is based on.
     *
     * @return a short description
     */
    override fun toString(): String {
        return """
             Auto-generated classifier wrapper, based on weka.classifiers.trees.J48 (generated with Weka 3.8.6).
             ${this.javaClass.name}/WekaClassifier
             """.trimIndent()
    }

    companion object {
        /**
         * Runs the classfier from commandline.
         *
         * @param args the commandline arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            runClassifier(WekaWrapper(), args)
        }
    }
}

internal object WekaClassifier {
    @Throws(Exception::class)
    fun classify(i: Array<Any?>): Double {
        var p = Double.NaN
        p = N5ab628c80(i)
        return p
    }

    fun N5ab628c80(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[0] == null) {
            p = 4.0
        } else if ((i[0] as Double?)!! <= 10.800627) {
            p = N69a9cbda1(i)
        } else if ((i[0] as Double?)!! > 10.800627) {
            p = N70b44d3438(i)
        }
        return p
    }

    fun N69a9cbda1(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[1] == null) {
            p = 0.0
        } else if ((i[1] as Double?)!! <= 2.605362) {
            p = N555a3f3e2(i)
        } else if ((i[1] as Double?)!! > 2.605362) {
            p = N6eb8fe4d21(i)
        }
        return p
    }

    fun N555a3f3e2(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[0] == null) {
            p = 4.0
        } else if ((i[0] as Double?)!! <= 9.791817) {
            p = N22be63ce3(i)
        } else if ((i[0] as Double?)!! > 9.791817) {
            p = N5d19a90e8(i)
        }
        return p
    }

    fun N22be63ce3(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[0] == null) {
            p = 4.0
        } else if ((i[0] as Double?)!! <= 9.750585) {
            p = N4925ef394(i)
        } else if ((i[0] as Double?)!! > 9.750585) {
            p = N3ba3c5647(i)
        }
        return p
    }

    fun N4925ef394(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[0] == null) {
            p = 1.0
        } else if ((i[0] as Double?)!! <= 9.686531) {
            p = N6ef51adc5(i)
        } else if ((i[0] as Double?)!! > 9.686531) {
            p = 4.0
        }
        return p
    }

    fun N6ef51adc5(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[1] == null) {
            p = 1.0
        } else if ((i[1] as Double?)!! <= 1.669662) {
            p = N6a526bf36(i)
        } else if ((i[1] as Double?)!! > 1.669662) {
            p = 2.0
        }
        return p
    }

    fun N6a526bf36(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[0] == null) {
            p = 2.0
        } else if ((i[0] as Double?)!! <= 8.65908) {
            p = 2.0
        } else if ((i[0] as Double?)!! > 8.65908) {
            p = 1.0
        }
        return p
    }

    fun N3ba3c5647(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[1] == null) {
            p = 3.0
        } else if ((i[1] as Double?)!! <= 1.150773) {
            p = 3.0
        } else if ((i[1] as Double?)!! > 1.150773) {
            p = 1.0
        }
        return p
    }

    fun N5d19a90e8(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[0] == null) {
            p = 0.0
        } else if ((i[0] as Double?)!! <= 9.923345) {
            p = N1b7c7b8b9(i)
        } else if ((i[0] as Double?)!! > 9.923345) {
            p = N7fef764218(i)
        }
        return p
    }

    fun N1b7c7b8b9(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[0] == null) {
            p = 0.0
        } else if ((i[0] as Double?)!! <= 9.846338) {
            p = N620886e410(i)
        } else if ((i[0] as Double?)!! > 9.846338) {
            p = N9a572f614(i)
        }
        return p
    }

    fun N620886e410(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[1] == null) {
            p = 0.0
        } else if ((i[1] as Double?)!! <= 1.159227) {
            p = N4725e72411(i)
        } else if ((i[1] as Double?)!! > 1.159227) {
            p = N2fbcfe4313(i)
        }
        return p
    }

    fun N4725e72411(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[1] == null) {
            p = 0.0
        } else if ((i[1] as Double?)!! <= 0.308426) {
            p = N57e4771712(i)
        } else if ((i[1] as Double?)!! > 0.308426) {
            p = 0.0
        }
        return p
    }

    fun N57e4771712(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[1] == null) {
            p = 0.0
        } else if ((i[1] as Double?)!! <= 0.033092) {
            p = 0.0
        } else if ((i[1] as Double?)!! > 0.033092) {
            p = 4.0
        }
        return p
    }

    fun N2fbcfe4313(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[1] == null) {
            p = 1.0
        } else if ((i[1] as Double?)!! <= 1.407366) {
            p = 1.0
        } else if ((i[1] as Double?)!! > 1.407366) {
            p = 0.0
        }
        return p
    }

    fun N9a572f614(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[1] == null) {
            p = 0.0
        } else if ((i[1] as Double?)!! <= 1.476457) {
            p = N6151b82115(i)
        } else if ((i[1] as Double?)!! > 1.476457) {
            p = 0.0
        }
        return p
    }

    fun N6151b82115(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[1] == null) {
            p = 0.0
        } else if ((i[1] as Double?)!! <= 0.601614) {
            p = N157927d316(i)
        } else if ((i[1] as Double?)!! > 0.601614) {
            p = 1.0
        }
        return p
    }

    fun N157927d316(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[0] == null) {
            p = 0.0
        } else if ((i[0] as Double?)!! <= 9.88889) {
            p = 0.0
        } else if ((i[0] as Double?)!! > 9.88889) {
            p = N5d7f8dd917(i)
        }
        return p
    }

    fun N5d7f8dd917(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[0] == null) {
            p = 1.0
        } else if ((i[0] as Double?)!! <= 9.909116) {
            p = 1.0
        } else if ((i[0] as Double?)!! > 9.909116) {
            p = 0.0
        }
        return p
    }

    fun N7fef764218(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[0] == null) {
            p = 4.0
        } else if ((i[0] as Double?)!! <= 9.995019) {
            p = N1393492e19(i)
        } else if ((i[0] as Double?)!! > 9.995019) {
            p = N4f08b33020(i)
        }
        return p
    }

    fun N1393492e19(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[0] == null) {
            p = 1.0
        } else if ((i[0] as Double?)!! <= 9.93377) {
            p = 1.0
        } else if ((i[0] as Double?)!! > 9.93377) {
            p = 4.0
        }
        return p
    }

    fun N4f08b33020(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[1] == null) {
            p = 1.0
        } else if ((i[1] as Double?)!! <= 2.040508) {
            p = 1.0
        } else if ((i[1] as Double?)!! > 2.040508) {
            p = 0.0
        }
        return p
    }

    fun N6eb8fe4d21(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[0] == null) {
            p = 4.0
        } else if ((i[0] as Double?)!! <= 10.018191) {
            p = N6a61c8be22(i)
        } else if ((i[0] as Double?)!! > 10.018191) {
            p = N1324513d35(i)
        }
        return p
    }

    fun N6a61c8be22(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[1] == null) {
            p = 4.0
        } else if ((i[1] as Double?)!! <= 28.155171) {
            p = N633236e523(i)
        } else if ((i[1] as Double?)!! > 28.155171) {
            p = 0.0
        }
        return p
    }

    fun N633236e523(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[1] == null) {
            p = 0.0
        } else if ((i[1] as Double?)!! <= 9.308755) {
            p = 0.0
        } else if ((i[1] as Double?)!! > 9.308755) {
            p = N6929988024(i)
        }
        return p
    }

    fun N6929988024(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[0] == null) {
            p = 4.0
        } else if ((i[0] as Double?)!! <= 9.91892) {
            p = N40ca597e25(i)
        } else if ((i[0] as Double?)!! > 9.91892) {
            p = 4.0
        }
        return p
    }

    fun N40ca597e25(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[0] == null) {
            p = 4.0
        } else if ((i[0] as Double?)!! <= 9.755311) {
            p = N240a4de126(i)
        } else if ((i[0] as Double?)!! > 9.755311) {
            p = N2f64460a31(i)
        }
        return p
    }

    fun N240a4de126(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[1] == null) {
            p = 4.0
        } else if ((i[1] as Double?)!! <= 15.528683) {
            p = N567dc29b27(i)
        } else if ((i[1] as Double?)!! > 15.528683) {
            p = N17b5377d30(i)
        }
        return p
    }

    fun N567dc29b27(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[0] == null) {
            p = 4.0
        } else if ((i[0] as Double?)!! <= 9.671159) {
            p = N7335bd4c28(i)
        } else if ((i[0] as Double?)!! > 9.671159) {
            p = 0.0
        }
        return p
    }

    fun N7335bd4c28(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[0] == null) {
            p = 4.0
        } else if ((i[0] as Double?)!! <= 9.605386) {
            p = N61260bfd29(i)
        } else if ((i[0] as Double?)!! > 9.605386) {
            p = 4.0
        }
        return p
    }

    fun N61260bfd29(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[0] == null) {
            p = 4.0
        } else if ((i[0] as Double?)!! <= 9.593783) {
            p = 4.0
        } else if ((i[0] as Double?)!! > 9.593783) {
            p = 0.0
        }
        return p
    }

    fun N17b5377d30(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[1] == null) {
            p = 4.0
        } else if ((i[1] as Double?)!! <= 22.251913) {
            p = 4.0
        } else if ((i[1] as Double?)!! > 22.251913) {
            p = 0.0
        }
        return p
    }

    fun N2f64460a31(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[1] == null) {
            p = 4.0
        } else if ((i[1] as Double?)!! <= 22.251913) {
            p = N71e5517732(i)
        } else if ((i[1] as Double?)!! > 22.251913) {
            p = N204dd55533(i)
        }
        return p
    }

    fun N71e5517732(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[1] == null) {
            p = 0.0
        } else if ((i[1] as Double?)!! <= 17.54561) {
            p = 0.0
        } else if ((i[1] as Double?)!! > 17.54561) {
            p = 4.0
        }
        return p
    }

    fun N204dd55533(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[0] == null) {
            p = 0.0
        } else if ((i[0] as Double?)!! <= 9.905575) {
            p = 0.0
        } else if ((i[0] as Double?)!! > 9.905575) {
            p = N1efd00fe34(i)
        }
        return p
    }

    fun N1efd00fe34(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[0] == null) {
            p = 4.0
        } else if ((i[0] as Double?)!! <= 9.914797) {
            p = 4.0
        } else if ((i[0] as Double?)!! > 9.914797) {
            p = 0.0
        }
        return p
    }

    fun N1324513d35(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[1] == null) {
            p = 0.0
        } else if ((i[1] as Double?)!! <= 19.465472) {
            p = 0.0
        } else if ((i[1] as Double?)!! > 19.465472) {
            p = N4891d4c736(i)
        }
        return p
    }

    fun N4891d4c736(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[1] == null) {
            p = 4.0
        } else if ((i[1] as Double?)!! <= 28.155171) {
            p = N63857fd37(i)
        } else if ((i[1] as Double?)!! > 28.155171) {
            p = 0.0
        }
        return p
    }

    fun N63857fd37(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[1] == null) {
            p = 0.0
        } else if ((i[1] as Double?)!! <= 24.288798) {
            p = 0.0
        } else if ((i[1] as Double?)!! > 24.288798) {
            p = 4.0
        }
        return p
    }

    fun N70b44d3438(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[0] == null) {
            p = 1.0
        } else if ((i[0] as Double?)!! <= 13.530957) {
            p = 1.0
        } else if ((i[0] as Double?)!! > 13.530957) {
            p = N412847a639(i)
        }
        return p
    }

    fun N412847a639(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[1] == null) {
            p = 1.0
        } else if ((i[1] as Double?)!! <= 1.392024) {
            p = N75984a840(i)
        } else if ((i[1] as Double?)!! > 1.392024) {
            p = 2.0
        }
        return p
    }

    fun N75984a840(i: Array<Any?>): Double {
        var p = Double.NaN
        if (i[0] == null) {
            p = 1.0
        } else if ((i[0] as Double?)!! <= 14.775756) {
            p = 1.0
        } else if ((i[0] as Double?)!! > 14.775756) {
            p = 2.0
        }
        return p
    }
}



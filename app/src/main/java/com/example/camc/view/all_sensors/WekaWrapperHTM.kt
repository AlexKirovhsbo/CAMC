package com.example.camc.view.all_sensors


// Import necessary libraries
import weka.core.Attribute
import weka.core.Capabilities
import weka.core.Capabilities.Capability
import weka.core.Instance
import weka.core.Instances
import weka.core.RevisionUtils
import weka.classifiers.Classifier
import weka.classifiers.AbstractClassifier
import java.util.logging.Logger

// Convert Java code to Kotlin
class WekaWrapper : AbstractClassifier() {
    fun globalInfo(): String {
        return toString()
    }

    override fun getCapabilities(): Capabilities {
        val result = Capabilities(this)

        result.enable(Capability.NOMINAL_ATTRIBUTES)
        result.enable(Capability.NUMERIC_ATTRIBUTES)
        result.enable(Capability.DATE_ATTRIBUTES)
        result.enable(Capability.MISSING_VALUES)
        result.enable(Capability.NOMINAL_CLASS)
        result.enable(Capability.MISSING_CLASS_VALUES)

        result.setMinimumNumberInstances(0)

        return result
    }

    override fun buildClassifier(i: Instances) {
        getCapabilities().testWithFail(i)
    }

    override fun classifyInstance(i: Instance): Double {
        val s = Array<Any?>(i.numAttributes()) { null }
        s.forEachIndexed { index, item ->
            logger.info("HALLLOOO[$index] = $item")
        }
        for (j in 0 until s.size) {
            if (!i.isMissing(j)) {
                if (i.attribute(j).isNominal())
                    s[j] = i.stringValue(j)
                else if (i.attribute(j).isNumeric())
                    s[j] = i.value(j)
            }
        }

        // set class value to missing
        s[i.classIndex()] = null

        return WekaClassifier.classify(s)
    }

    override fun getRevision(): String {
        return RevisionUtils.extract("1.0")
    }

    override fun toString(): String {
        return "Auto-generated classifier wrapper, based on weka.classifiers.trees.J48 (generated with Weka 3.8.6).\n" + this.javaClass.name + "/WekaClassifier"
    }

    companion object {
        val logger = Logger.getLogger(AllSensorsViewModel::class.java.name)
        fun main(args: Array<String>) {
            runClassifier(WekaWrapper(), args)
        }
    }
}

class WekaClassifier {

    companion object {
        @Throws(Exception::class)
        fun classify(i: Array<Any?>): Double {
            var p = Double.NaN
            p = N7b447cee0(i)
            return p
        }

        private fun N7b447cee0(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 0.0
            } else if ((i[1] as Double) <= 1.5) {
                p = N6884900e1(i)
            } else if ((i[1] as Double) > 1.5) {
                p = N68bd364329(i)
            }
            return p
        }

        private fun N6884900e1(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[3] == null) {
                p = 2.0
            } else if ((i[3] as Double) <= 10.10834065556011) {
                p = N3e24624f2(i)
            } else if ((i[3] as Double) > 10.10834065556011) {
                p = N797fd55d17(i)
            }
            return p
        }

        private fun N3e24624f2(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[3] == null) {
                p = 0.0
            } else if ((i[3] as Double) <= 9.725809188790992) {
                p = N1cf32c273(i)
            } else if ((i[3] as Double) > 9.725809188790992) {
                p = N7edbaeb413(i)
            }
            return p
        }

        private fun N1cf32c273(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 1.0
            } else if ((i[1] as Double) <= 0.91) {
                p = N4488805b4(i)
            } else if ((i[1] as Double) > 0.91) {
                p = N156b51796(i)
            }
            return p
        }

        private fun N4488805b4(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 0.0
            } else if ((i[1] as Double) <= 0.47) {
                p = N8407c245(i)
            } else if ((i[1] as Double) > 0.47) {
                p = 1.0
            }
            return p
        }

        private fun N8407c245(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[3] == null) {
                p = 1.0
            } else if ((i[3] as Double) <= 7.271891261465903) {
                p = 1.0
            } else if ((i[3] as Double) > 7.271891261465903) {
                p = 0.0
            }
            return p
        }

        private fun N156b51796(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 0.0
            } else if ((i[1] as Double) <= 1.39) {
                p = N107cd5e77(i)
            } else if ((i[1] as Double) > 1.39) {
                p = N2af347e12(i)
            }
            return p
        }

        private fun N107cd5e77(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 0.0
            } else if ((i[1] as Double) <= 1.32) {
                p = N107615eb8(i)
            } else if ((i[1] as Double) > 1.32) {
                p = 0.0
            }
            return p
        }

        private fun N107615eb8(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 0.0
            } else if ((i[1] as Double) <= 1.31) {
                p = N1bdcea9d9(i)
            } else if ((i[1] as Double) > 1.31) {
                p = N22d6a48211(i)
            }
            return p
        }

        private fun N1bdcea9d9(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[3] == null) {
                p = 0.0
            } else if ((i[3] as Double) <= 5.23711861206545) {
                p = N5473900610(i)
            } else if ((i[3] as Double) > 5.23711861206545) {
                p = 0.0
            }
            return p
        }

        private fun N5473900610(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[3] == null) {
                p = 1.0
            } else if ((i[3] as Double) <= 3.328174595955562) {
                p = 1.0
            } else if ((i[3] as Double) > 3.328174595955562) {
                p = 0.0
            }
            return p
        }

        private fun N22d6a48211(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[3] == null) {
                p = 1.0
            } else if ((i[3] as Double) <= 6.631789084394384) {
                p = 1.0
            } else if ((i[3] as Double) > 6.631789084394384) {
                p = 0.0
            }
            return p
        }

        private fun N2af347e12(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 1.0
            } else if ((i[1] as Double) <= 1.44) {
                p = 1.0
            } else if ((i[1] as Double) > 1.44) {
                p = 0.0
            }
            return p
        }

        private fun N7edbaeb413(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[3] == null) {
                p = 2.0
            } else if ((i[3] as Double) <= 9.925946083618596) {
                p = 2.0
            } else if ((i[3] as Double) > 9.925946083618596) {
                p = N36e0f2c914(i)
            }
            return p
        }

        private fun N36e0f2c914(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[3] == null) {
                p = 2.0
            } else if ((i[3] as Double) <= 9.976602249617615) {
                p = 2.0
            } else if ((i[3] as Double) > 9.976602249617615) {
                p = N2185a22315(i)
            }
            return p
        }

        private fun N2185a22315(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 0.0
            } else if ((i[1] as Double) <= 0.91) {
                p = N5a726ea916(i)
            } else if ((i[1] as Double) > 0.91) {
                p = 0.0
            }
            return p
        }

        private fun N5a726ea916(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 0.0
            } else if ((i[1] as Double) <= 0.47) {
                p = 0.0
            } else if ((i[1] as Double) > 0.47) {
                p = 1.0
            }
            return p
        }

        private fun N797fd55d17(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 1.0
            } else if ((i[1] as Double) <= 0.91) {
                p = N48b8160118(i)
            } else if ((i[1] as Double) > 0.91) {
                p = Nd6e188420(i)
            }
            return p
        }

        private fun N48b8160118(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 0.0
            } else if ((i[1] as Double) <= 0.47) {
                p = N4420218f19(i)
            } else if ((i[1] as Double) > 0.47) {
                p = 1.0
            }
            return p
        }

        private fun N4420218f19(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[3] == null) {
                p = 0.0
            } else if ((i[3] as Double) <= 13.9961667022018) {
                p = 0.0
            } else if ((i[3] as Double) > 13.9961667022018) {
                p = 1.0
            }
            return p
        }

        private fun Nd6e188420(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 0.0
            } else if ((i[1] as Double) <= 1.39) {
                p = Na3ce14921(i)
            } else if ((i[1] as Double) > 1.39) {
                p = N599235b727(i)
            }
            return p
        }

        private fun Na3ce14921(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 0.0
            } else if ((i[1] as Double) <= 1.32) {
                p = N23cf61122(i)
            } else if ((i[1] as Double) > 1.32) {
                p = 0.0
            }
            return p
        }

        private fun N23cf61122(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 0.0
            } else if ((i[1] as Double) <= 1.31) {
                p = N47d5838023(i)
            } else if ((i[1] as Double) > 1.31) {
                p = N323079f226(i)
            }
            return p
        }

        private fun N47d5838023(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[3] == null) {
                p = 0.0
            } else if ((i[3] as Double) <= 15.20876935453939) {
                p = 0.0
            } else if ((i[3] as Double) > 15.20876935453939) {
                p = N3a8209c624(i)
            }
            return p
        }

        private fun N3a8209c624(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[3] == null) {
                p = 0.0
            } else if ((i[3] as Double) <= 19.78233408962514) {
                p = 0.0
            } else if ((i[3] as Double) > 19.78233408962514) {
                p = N5759917625(i)
            }
            return p
        }

        private fun N5759917625(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[3] == null) {
                p = 0.0
            } else if ((i[3] as Double) <= 19.906152422031283) {
                p = 0.0
            } else if ((i[3] as Double) > 19.906152422031283) {
                p = 1.0
            }
            return p
        }

        private fun N323079f226(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[3] == null) {
                p = 0.0
            } else if ((i[3] as Double) <= 14.742764317559029) {
                p = 0.0
            } else if ((i[3] as Double) > 14.742764317559029) {
                p = 1.0
            }
            return p
        }

        private fun N599235b727(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 1.0
            } else if ((i[1] as Double) <= 1.44) {
                p = 1.0
            } else if ((i[1] as Double) > 1.44) {
                p = N7374cb7a28(i)
            }
            return p
        }

        private fun N7374cb7a28(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[3] == null) {
                p = 0.0
            } else if ((i[3] as Double) <= 12.866017812480052) {
                p = 0.0
            } else if ((i[3] as Double) > 12.866017812480052) {
                p = 1.0
            }
            return p
        }

        private fun N68bd364329(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[3] == null) {
                p = 1.0
            } else if ((i[3] as Double) <= 9.66465898070478) {
                p = 1.0
            } else if ((i[3] as Double) > 9.66465898070478) {
                p = N59f32d4f30(i)
            }
            return p
        }

        private fun N59f32d4f30(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[3] == null) {
                p = 2.0
            } else if ((i[3] as Double) <= 10.077965711681404) {
                p = 2.0
            } else if ((i[3] as Double) > 10.077965711681404) {
                p = 1.0
            }
            return p
        }
    }
}


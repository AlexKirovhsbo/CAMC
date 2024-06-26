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
            p = N23ac15390(i)
            return p
        }

        fun N23ac15390(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 2.0
            } else if ((i[1] as Double) <= 0.0) {
                p = N4be3f0d21(i)
            } else if ((i[1] as Double) > 0.0) {
                p = N7e4dfbc13(i)
            }
            return p
        }

        fun N4be3f0d21(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[2] == null) {
                p = 2.0
            } else if ((i[2] as Double) <= 10.136029169279189) {
                p = 2.0
            } else if ((i[2] as Double) > 10.136029169279189) {
                p = N2b6ea54c2(i)
            }
            return p
        }

        fun N2b6ea54c2(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[2] == null) {
                p = 2.0
            } else if ((i[2] as Double) <= 14.653306956125615) {
                p = 2.0
            } else if ((i[2] as Double) > 14.653306956125615) {
                p = 1.0
            }
            return p
        }

        fun N7e4dfbc13(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 0.0
            } else if ((i[1] as Double) <= 1.5) {
                p = N3d0b3cec4(i)
            } else if ((i[1] as Double) > 1.5) {
                p = 1.0
            }
            return p
        }

        fun N3d0b3cec4(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 1.0
            } else if ((i[1] as Double) <= 0.91) {
                p = N69ba2eff5(i)
            } else if ((i[1] as Double) > 0.91) {
                p = N7a5f638f9(i)
            }
            return p
        }

        fun N69ba2eff5(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 0.0
            } else if ((i[1] as Double) <= 0.47) {
                p = N3720a2416(i)
            } else if ((i[1] as Double) > 0.47) {
                p = 1.0
            }
            return p
        }

        fun N3720a2416(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[2] == null) {
                p = 0.0
            } else if ((i[2] as Double) <= 13.9961667022018) {
                p = N2622d4977(i)
            } else if ((i[2] as Double) > 13.9961667022018) {
                p = 1.0
            }
            return p
        }

        fun N2622d4977(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[2] == null) {
                p = 0.0
            } else if ((i[2] as Double) <= 7.616120704307761) {
                p = N422a60e38(i)
            } else if ((i[2] as Double) > 7.616120704307761) {
                p = 0.0
            }
            return p
        }

        fun N422a60e38(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[2] == null) {
                p = 1.0
            } else if ((i[2] as Double) <= 7.247434256175666) {
                p = 1.0
            } else if ((i[2] as Double) > 7.247434256175666) {
                p = 0.0
            }
            return p
        }

        fun N7a5f638f9(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 0.0
            } else if ((i[1] as Double) <= 1.39) {
                p = N48dff42f10(i)
            } else if ((i[1] as Double) > 1.39) {
                p = N6fb9c40d15(i)
            }
            return p
        }

        fun N48dff42f10(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 0.0
            } else if ((i[1] as Double) <= 1.32) {
                p = N786907cf11(i)
            } else if ((i[1] as Double) > 1.32) {
                p = 0.0
            }
            return p
        }

        fun N786907cf11(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 0.0
            } else if ((i[1] as Double) <= 1.31) {
                p = N69d54d5912(i)
            } else if ((i[1] as Double) > 1.31) {
                p = N681718f713(i)
            }
            return p
        }

        fun N69d54d5912(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[2] == null) {
                p = 0.0
            } else if ((i[2] as Double) <= 19.906152422031283) {
                p = 0.0
            } else if ((i[2] as Double) > 19.906152422031283) {
                p = 1.0
            }
            return p
        }

        fun N681718f713(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[2] == null) {
                p = 1.0
            } else if ((i[2] as Double) <= 6.631789084394384) {
                p = 1.0
            } else if ((i[2] as Double) > 6.631789084394384) {
                p = N5f5ef75614(i)
            }
            return p
        }

        fun N5f5ef75614(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[2] == null) {
                p = 0.0
            } else if ((i[2] as Double) <= 14.742764317559029) {
                p = 0.0
            } else if ((i[2] as Double) > 14.742764317559029) {
                p = 1.0
            }
            return p
        }

        fun N6fb9c40d15(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[1] == null) {
                p = 1.0
            } else if ((i[1] as Double) <= 1.44) {
                p = 1.0
            } else if ((i[1] as Double) > 1.44) {
                p = N2cc5f32616(i)
            }
            return p
        }

        fun N2cc5f32616(i: Array<Any?>): Double {
            var p = Double.NaN
            if (i[2] == null) {
                p = 0.0
            } else if ((i[2] as Double) <= 12.839708254451695) {
                p = 0.0
            } else if ((i[2] as Double) > 12.839708254451695) {
                p = 1.0
            }
            return p
        }
    }
}



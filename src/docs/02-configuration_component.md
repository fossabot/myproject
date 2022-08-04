# Configuration component

The configuration in any project, is sometime a mess. Here is a way to maintain and delegate such subject to a dedicated
component, but accessible to every other as source of truth for configuration attributes values and default values.

## Some requirement

First, some requirements are identified, mandatory ones and optional ones.

### Mandatory

|  # UC  | Description                                                                                                             |
|:------:|:------------------------------------------------------------------------------------------------------------------------|
| UC0201 | Configuration values are stored in a properties file                                                                    |
| UC0202 | Configuration attributes are attributes of the `Configuration` component                                                |
| UC0203 | In case no file is provided (not existing or not accessible), the configuration will go with hard coded default values. |
| UC0204 | Configuration loaded values can be overloaded by command line arguments values, at start time.                          |
| UC0205 | Configuration must support String, Integer and Double as values.                                                        |

### Optional

|  # UC  | Description                                                          |
|:------:|:---------------------------------------------------------------------|
| UC0206 | If configuration file change, the values are automatically reloaded. |

